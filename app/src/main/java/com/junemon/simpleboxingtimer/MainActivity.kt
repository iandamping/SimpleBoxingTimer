package com.junemon.simpleboxingtimer

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.InterstitialAd
import com.junemon.simpleboxingtimer.TimerConstant.setCustomMinutes
import com.junemon.simpleboxingtimer.TimerConstant.setCustomSeconds
import com.junemon.simpleboxingtimer.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import org.koin.androidx.scope.lifecycleScope as koinLifecycleScope

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var mInterstitialAd: InterstitialAd

    private val vm: MainViewmodel by koinLifecycleScope.inject()

    private val listRestTime by lazy { resources.getStringArray(R.array.rest_time) }
    private val listRoundTime by lazy { resources.getStringArray(R.array.round_time) }
    private val listWhichRound by lazy { resources.getStringArray(R.array.which_round) }
    private var warningValue: Int? = null
    private var roundTimeValue: Long = 0
    private var restTimeValue: Long = 0
    private var howMuchRoundValue: Int = 0
    private var isTimerRunning = false
    private var howMuchRoundCounter: Int = 0

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
        observeCurrentRound()
        observeWarningValue()
    }

    private fun observeIsTimmerRunning() {
        lifecycleScope.launchWhenStarted {
            vm.isTimerRunning.collect {
                isTimerRunning = it
                binding.disableView(isTimerRunning)
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
                Log.e("counter", "current value : $howMuchRoundValue")
/*
                    Log.e("counter", "current value : $howMuchRoundValue")
                    Log.e("counter", "current value : $roundTimeController")

                    if (restTimeValue.equals(0)) {
                        when {
                            howMuchRoundCounter < howMuchRoundValue -> {
                                vm.startTimer(restTimeValue) {
                                    howMuchRoundCounter++
                                    vm.setWhichRound(roundTimeController)
                                    Log.e("counter", "done running")
                                }
                            }
                            else -> {
                                howMuchRoundCounter = 0
                                roundTimeController = 0
                                with(binding) {
                                    timerSet = null
                                    isRest = false
                                }
                            }
                        }
                    } else {
                        when {
                            howMuchRoundCounter < howMuchRoundValue -> {
                                vm.startRestTimer(restTimeValue) {
                                    howMuchRoundCounter++
                                    vm.setWhichRound(roundTimeController)
                                    Log.e("counter", "done running")
                                }
                            }
                            else -> {
                                howMuchRoundCounter = 0
                                roundTimeController = 0
                                with(binding) {
                                    timerSet = null
                                    isRest = false
                                }
                            }
                        }
                    }*/

                /*vm.startTimer(roundTimeValue) {
                    howMuchRoundCounter++
                    vm.setWhichRound(roundTimeController)

                    if (restTimeValue.equals(0)) {
                        when{
                            howMuchRoundCounter < howMuchRoundValue ->{
                                vm.startTimer(restTimeValue) {
                                    vm.setWhichRound(roundTimeController)
                                    Log.e("counter","done running")
                                }
                            }
                            else ->{
                                howMuchRoundCounter = 0
                                roundTimeController = 0
                                with(binding){
                                    timerSet = null
                                    isRest = false
                                }
                            }
                        }

                    } else {
                        when{
                            howMuchRoundCounter < howMuchRoundValue ->{
                                vm.startRestTimer(restTimeValue) {
                                    vm.setWhichRound(roundTimeController)
                                    Log.e("counter","done running")
                                }
                            }
                            else ->{
                                howMuchRoundCounter = 0
                                roundTimeController = 0
                                with(binding){
                                    timerSet = null
                                    isRest = false
                                }
                            }
                        }
                    }
                }*/
            }
        }
    }

    private fun observeCurrentRound() {
        lifecycleScope.launchWhenStarted {
            vm.currentRound.collect { currentRounds ->
                binding.currentRound = "Round $currentRounds / $howMuchRoundValue"
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
        initNumberPicker()
        initRadioButton()
        initTimer()

        btnReset.setOnClickListener {
            startActivity<MainActivity>()
            finish()
        }

        btnStart.setOnClickListener {
            startTimer()
        }
    }

    private fun startTimer() {
        vm.startTimer(roundTimeValue) {
            howMuchRoundCounter++
            if (restTimeValue.equals(0)) {
                when {
                    howMuchRoundCounter < howMuchRoundValue -> {
                        startTimer()
                    }
                    else -> {
                        howMuchRoundCounter = 0
                        with(binding) {
                            timerSet = null
                            isRest = false
                        }
                    }
                }
            } else {
                when {
                    howMuchRoundCounter < howMuchRoundValue -> {
                        vm.startRestTimer(restTimeValue) {
                            startTimer()
                        }
                    }
                    else -> {
                        howMuchRoundCounter = 0
                        with(binding) {
                            timerSet = null
                            isRest = false
                        }
                    }
                }
            }
        }

    }

    private fun ActivityMainBinding.initTimer() {
        lifecycleScope.launchWhenStarted {
            vm.currentTime.collect { timeTicking ->
                timeTicking?.let {
                    val value = DateUtils.formatElapsedTime(it)
                    if (warningValue != null) {
                        if (value == "00:$warningValue") {
                            vm.warningBellSound()
                        }
                    }
                    isRest = false
                    timerSet = value
                }
            }

            vm.currentRestTime.collect { restTimeTicking ->
                Log.e("timer", "timer ticking : $restTimeTicking")
                restTimeTicking?.let {
                    val value = DateUtils.formatElapsedTime(it)
                    when {
                        value != "00:00" -> {
                            isRest = true
                            timerSet = value
                        }
                    }
                }
            }

        }
    }

    private fun ActivityMainBinding.disableView(isRunning: Boolean) {
        btnStart.isEnabled = !isRunning
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

    private fun timerCounter() {
    }
}

