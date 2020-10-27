package com.junemon.simpleboxingtimer

import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.junemon.simpleboxingtimer.databinding.ActivityMainBinding
import com.junemon.simpleboxingtimer.util.GenericPair
import com.junemon.simpleboxingtimer.util.TimerConstant.FLAGS_FULLSCREEN
import com.junemon.simpleboxingtimer.util.TimerConstant.REST_TIME_STATE
import com.junemon.simpleboxingtimer.util.TimerConstant.ROUND_TIME_STATE
import com.junemon.simpleboxingtimer.util.TimerConstant.setCustomMinutes
import com.junemon.simpleboxingtimer.util.TimerConstant.setCustomSeconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import org.koin.androidx.scope.lifecycleScope as koinLifecycleScope

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val MY_REQUEST_CODE: Int = 0
    private lateinit var mInterstitialAd: InterstitialAd

    private val vm: MainViewmodel by koinLifecycleScope.inject()
    private val IMMERSIVE_FLAG_TIMEOUT = 500L
    private val listRestTime by lazy { resources.getStringArray(R.array.rest_time) }
    private val listRoundTime by lazy { resources.getStringArray(R.array.round_time) }
    private val listWhichRound by lazy { resources.getStringArray(R.array.which_round) }
    private var warningValue: Int? = null
    private var roundTimeValue: Long = 0
    private var restTimeValue: Long = 0
    private var howMuchRoundValue: Int = 0
    private var howMuchRoundCounter: Int = 0

    private var roundState: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            lifecycleOwner = this@MainActivity
            initView()
        }

        observeIsTimmerRunning()
        observeRoundTimeValue()
        observeRestTimeValue()
        observeWhichRoundValue()
        observeWarningValue()
        observeTimer()
    }

    override fun onResume() {
        super.onResume()
        checkUpdate()
        // Before setting full screen flags, we must wait a bit to let UI settle; otherwise, we may
        // be trying to set app to immersive mode before it's ready and the flags do not stick
        binding.root.postDelayed({
            binding.root.systemUiVisibility = FLAGS_FULLSCREEN
        }, IMMERSIVE_FLAG_TIMEOUT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Timber.e("Update flow failed! Result code: $resultCode")
                // If the update is cancelled or fails,
                // you can request to start the update again.
                checkUpdate()
            }
        }
    }

    private fun observeIsTimmerRunning() {
        lifecycleScope.launchWhenStarted {
            vm.isTimerRunning.collect {
                binding.disableView(it)
            }
        }
    }

    private fun observeRestTimeValue() {
        lifecycleScope.launchWhenStarted {
            vm.restTimeValue.collect { restTimeResult ->
                restTimeValue = restTimeResult
            }
        }
    }

    private fun observeRoundTimeValue() {
        lifecycleScope.launchWhenStarted {
            vm.roundTimeValue.collect { roundTimeResult ->
                when (roundTimeResult) {
                    0 -> roundTimeValue = setCustomSeconds(30)
                    1 -> roundTimeValue = setCustomMinutes(1)
                    2 -> roundTimeValue = setCustomMinutes(2)
                    3 -> roundTimeValue = setCustomMinutes(3)
                    4 -> roundTimeValue = setCustomMinutes(4)
                    5 -> roundTimeValue = setCustomMinutes(5)
                    6 -> roundTimeValue = setCustomMinutes(6)
                    7 -> roundTimeValue = setCustomMinutes(7)
                    8 -> roundTimeValue = setCustomMinutes(8)
                    9 -> roundTimeValue = setCustomMinutes(9)
                    10 -> roundTimeValue = setCustomMinutes(10)
                }
            }
        }
    }

    private fun observeWhichRoundValue() {
        lifecycleScope.launchWhenStarted {
            vm.whichRoundValue.collect { roundValueResult ->
                howMuchRoundValue = roundValueResult
            }
        }
    }

    private fun observeWarningValue() {
        lifecycleScope.launchWhenStarted {
            vm.warningValue.collect {
                warningValue = it
            }
        }
    }

    private fun ActivityMainBinding.initView() {
        currentRound = "Round 0 / 0"
        initNumberPicker()
        initRadioButton()
        // inflateAdsView()

        btnReset.setOnClickListener {
            howMuchRoundCounter = 0
            with(vm) {
                cancelAllTimer()
                setTimmerIsRunning(false)
                setWarningValue(0)
            }
            with(binding) {
                currentRound = "Round 0 / 0"
                timerSet = null
                isRest = false
            }

            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        btnStart.setOnClickListener {
            startTimer()
        }
        btnStop.setOnClickListener {
            // vm.setTimmerIsRunning(false)
            // vm.cancelAllTimer()
        }
    }

    private fun startTimer() {
        if (howMuchRoundCounter == 0) {
            binding.currentRound = "Round 1 / $howMuchRoundValue"
        }
        with(vm){
            setTimmerIsRunning(true)
            startBellSound()
            when(roundState){
                ROUND_TIME_STATE ->{
                    vm.startTimer(this@MainActivity.roundTimeValue) {
                        howMuchRoundCounter++
                        when {
                            howMuchRoundCounter < howMuchRoundValue -> {
                                startingTimer(REST_TIME_STATE)
                            }
                            else -> {
                                finishedTimer()
                            }
                        }
                    }
                }
                REST_TIME_STATE ->{
                    vm.startTimer(this@MainActivity.restTimeValue) {
                        when {
                            howMuchRoundCounter < howMuchRoundValue -> {
                                binding.currentRound = "Round ${howMuchRoundCounter + 1} / $howMuchRoundValue"
                                startingTimer(ROUND_TIME_STATE)
                            }
                            else -> {
                                finishedTimer()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun finishedTimer() {
        howMuchRoundCounter = 0
        with(binding) {
            currentRound = "Round 0 / 0"
            timerSet = null
            isRest = false
        }
        with(vm) {
            setTimmerIsRunning(false)
            setIsRoundTimeRunning(ROUND_TIME_STATE)
            setWarningValue(0)
        }
    }

    private fun startingTimer(roundState:Int){
        if (this@MainActivity.restTimeValue == 0L) {
            startTimer()
        } else {
            with(vm){
                endBellSound()
                setIsRoundTimeRunning(roundState)
            }
            startTimer()
        }
    }

    private fun observeTimer() {
        lifecycleScope.launch {
            vm.roundTimeState.combine(vm.currentTime){ state,timeTicking ->
                GenericPair(state,timeTicking)
            }.collect {
                roundState = it.data1

                when(it.data1){
                    ROUND_TIME_STATE ->{
                        it.data2?.let {timeTicking ->
                            val value = DateUtils.formatElapsedTime(timeTicking)
                            if (warningValue != null) {
                                if ( value == "00:$warningValue"){
                                    vm.warningBellSound()
                                }
                            }
                            with(binding) {
                                isRest = false
                                timerSet = value
                            }
                        }
                    }
                    REST_TIME_STATE ->{
                        it.data2?.let {timeTicking ->
                            val value = DateUtils.formatElapsedTime(timeTicking)
                            if (value != "00:00"){
                                with(binding) {
                                    isRest = true
                                    timerSet = value
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private fun ActivityMainBinding.disableView(isRunning: Boolean) {
        if (isRunning) {
            btnStop.visibility = View.VISIBLE
            btnStart.visibility = View.GONE
        } else {
            btnStop.visibility = View.GONE
            btnStart.visibility = View.VISIBLE
        }
        // btnStart.isEnabled = !isRunning
        radioOff.isEnabled = !isRunning
        radioTenSec.isEnabled = !isRunning
        radioThirtySec.isEnabled = !isRunning
    }

    private fun ActivityMainBinding.initNumberPicker() {
        npRestTime.apply {
            minValue = 0
            maxValue = listRestTime.size - 1
            displayedValues = listRestTime
            if (value == 0) {
                vm.setRestTime(setCustomSeconds(0))
            }
            setOnValueChangedListener { _, _, newVal ->
                when (newVal) {
                    0 -> vm.setRestTime(setCustomSeconds(0))
                    1 -> vm.setRestTime(setCustomSeconds(15))
                    2 -> vm.setRestTime(setCustomSeconds(30))
                    3 -> vm.setRestTime(setCustomSeconds(60))
                    4 -> vm.setRestTime(setCustomSeconds(90))
                    5 -> vm.setRestTime(setCustomSeconds(120))
                    6 -> vm.setRestTime(setCustomSeconds(150))
                    7 -> vm.setRestTime(setCustomSeconds(180))
                }
            }

        }

        npRoundTime.apply {
            minValue = 0
            maxValue = listRoundTime.size - 1
            displayedValues = listRoundTime
            vm.setRoundTime(value)
            setOnValueChangedListener { _, _, newVal ->
                vm.setRoundTime(newVal)
            }
        }
        npWhichRound.apply {
            minValue = 0
            maxValue = listWhichRound.size - 1
            displayedValues = listWhichRound
            vm.setWhichRound(value + 1)
            setOnValueChangedListener { _, _, newVal ->
                vm.setWhichRound(newVal + 1)
            }
        }
    }

    private fun ActivityMainBinding.initRadioButton() {
        radioGroupRoot.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioOff -> vm.setWarningValue(0)
                R.id.radioTenSec -> vm.setWarningValue(10)
                R.id.radioThirtySec -> vm.setWarningValue(30)
            }
        }
    }

    private fun ActivityMainBinding.inflateAdsView() {
        MobileAds.initialize(this@MainActivity) {
            Timber.e("init ads!")
        }
        val request = AdRequest.Builder().build()
        detailAdView.loadAd(request)
    }

    private fun checkUpdate() {
        val updateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = updateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                updateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE, this, MY_REQUEST_CODE
                )
            }
        }
    }
}

