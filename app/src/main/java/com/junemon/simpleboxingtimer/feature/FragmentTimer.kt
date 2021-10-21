package com.junemon.simpleboxingtimer.feature

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.junemon.simpleboxingtimer.MainActivity
import com.junemon.simpleboxingtimer.MainViewmodel
import com.junemon.simpleboxingtimer.R
import com.junemon.simpleboxingtimer.base.BaseFragment
import com.junemon.simpleboxingtimer.databinding.FragmentTimerBinding
import com.junemon.simpleboxingtimer.util.GenericPair
import com.junemon.simpleboxingtimer.util.TimerConstant
import com.junemon.simpleboxingtimer.util.TimerConstant.DEFAULT_INTEGER_VALUE
import com.junemon.simpleboxingtimer.util.TimerConstant.DEFAULT_LONG_VALUE
import com.junemon.simpleboxingtimer.util.TimerConstant.ONE_SECOND
import com.junemon.simpleboxingtimer.util.TimerConstant.REST_TIME_STATE
import com.junemon.simpleboxingtimer.util.TimerConstant.ROUND_TIME_STATE
import com.junemon.simpleboxingtimer.util.clicks
import com.junemon.simpleboxingtimer.util.startActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

/**
 * Created by Ian Damping on 06,May,2021
 * Github https://github.com/iandamping
 * Indonesia.
 */
@AndroidEntryPoint
class FragmentTimer : BaseFragment<FragmentTimerBinding>() {

    private val vm: MainViewmodel by viewModels()

    private val listRestTime by lazy { resources.getStringArray(R.array.rest_time) }
    private val listRoundTime by lazy { resources.getStringArray(R.array.round_time) }
    private val listWhichRound by lazy { resources.getStringArray(R.array.which_round) }
    private var warningValue: Int? = null
    private var roundTimeValue: Long = DEFAULT_LONG_VALUE
    private var restTimeValue: Long = DEFAULT_LONG_VALUE
    private var howMuchRoundValue: Int = DEFAULT_INTEGER_VALUE
    private var howMuchRoundCounter: Int = DEFAULT_INTEGER_VALUE
    private var pausedTimeValue: Int = DEFAULT_INTEGER_VALUE
    private var roundState: Int = DEFAULT_INTEGER_VALUE


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTimerBinding
        get() = FragmentTimerBinding::inflate

    override fun viewCreated() {
        binding.initView()
    }

    override fun activityCreated() {
        observeIsTimmerRunning()
        observeRoundTimeValue()
        observeRestTimeValue()
        observeWhichRoundValue()
        observeWarningValue()
        observeTimer()
        observePausedTime()
    }

    private fun observeIsTimmerRunning() {
        vm.isTimerRunning.observe(viewLifecycleOwner) {
            binding.disableView(it)
        }
    }

    private fun observeRestTimeValue() {
        vm.restTimeValue.observe(viewLifecycleOwner) { restTimeResult ->
            restTimeValue = restTimeResult
        }
    }

    private fun observeRoundTimeValue() {
        vm.roundTimeValue.observe(viewLifecycleOwner) { roundTimeResult ->
            roundTimeValue = roundTimeResult
        }
    }

    private fun observeWhichRoundValue() {
        vm.whichRoundValue.observe(viewLifecycleOwner) { roundValueResult ->
            howMuchRoundValue = roundValueResult
        }
    }

    private fun observeWarningValue() {
        vm.warningValue.observe(viewLifecycleOwner) {
            warningValue = it
        }
    }

    private fun observePausedTime() {
        vm.pausedTime.observe(viewLifecycleOwner) { timeTicking ->
            pausedTimeValue = timeTicking.toInt()
        }
    }

    private fun FragmentTimerBinding.initView() {
        currentRound = "Round 0 / 0"
        initNumberPicker()
        getNumberPicker()
        initRadioButton()
        inflateAdsView()

        clicks(btnReset){
            resettingAll()
        }

        clicks(btnStart) {
            startTimer()
            if (btnStart.isVisible) {
                btnStart.visibility = View.GONE
                btnStop.visibility = View.VISIBLE
            }
        }
        clicks(btnStop) {
            vm.cancelAllTimer()
            if (btnStop.isVisible) {
                btnStop.visibility = View.GONE
                btnStart.visibility = View.VISIBLE
            }
        }
    }

    private fun resettingAll() {
        with(vm) {
            cancelAllTimer()
        }


        with(requireActivity()) {
            startActivity<MainActivity>()
            finish()
        }
    }

    private fun startTimer() {
        if (howMuchRoundCounter == 0) {
            binding.currentRound = "Round 1 / $howMuchRoundValue"
        }
        with(vm) {
            setTimmerIsRunning(true)
            when (roundState) {
                ROUND_TIME_STATE -> {
                    if (pausedTimeValue != DEFAULT_INTEGER_VALUE) {
                        vm.startTimer(pausedTimeValue * ONE_SECOND) {
                            howMuchRoundCounter++
                            pausedTimeValue = DEFAULT_INTEGER_VALUE
                            when {
                                howMuchRoundCounter < howMuchRoundValue -> {
                                    startingTimerForRoundOnly()
                                }
                                else -> {
                                    finishedTimer()
                                }
                            }
                        }
                    } else {
                        startBellSound()
                        vm.startTimer(this@FragmentTimer.roundTimeValue) {
                            howMuchRoundCounter++
                            when {
                                howMuchRoundCounter < howMuchRoundValue -> {
                                    startingTimerForRoundOnly()
                                }
                                else -> {
                                    finishedTimer()
                                }
                            }
                        }
                    }
                }
                REST_TIME_STATE -> {
                    if (pausedTimeValue != DEFAULT_INTEGER_VALUE) {
                        vm.startTimer(pausedTimeValue * ONE_SECOND) {
                            pausedTimeValue = DEFAULT_INTEGER_VALUE
                            startingTimerForRestOnly()
                        }
                    } else {
                        vm.startTimer(this@FragmentTimer.restTimeValue) {
                            startingTimerForRestOnly()
                        }
                    }
                }
            }
        }
    }

    private fun finishedTimer() {
        with(binding) {
            currentRound = "Round 0 / 0"
            timerSet = null
            isRest = false
            radioGroupRoot.clearCheck()
            radioGroupRoot.check(R.id.radioOff)
            if (btnStop.isVisible) {
                btnStop.visibility = View.GONE
                btnStart.visibility = View.VISIBLE
            }
        }
        with(vm) {
            endBellSound()
            cancelAllTimer()
            setTimmerIsRunning(false)
            setIsRoundTimeRunning(ROUND_TIME_STATE)
            setWarningValue(DEFAULT_INTEGER_VALUE)
        }

        howMuchRoundCounter = DEFAULT_INTEGER_VALUE

    }

    private fun startingTimerForRoundOnly() {
        if (restTimeValue == DEFAULT_LONG_VALUE) {
            binding.currentRound = "Round ${howMuchRoundCounter + 1} / $howMuchRoundValue"
            vm.setIsRoundTimeRunning(ROUND_TIME_STATE)
            startTimer()
        } else {
            with(vm) {
                endBellSound()
                setIsRoundTimeRunning(REST_TIME_STATE)
            }
            startTimer()
        }
    }

    private fun startingTimerForRestOnly() {
        binding.currentRound = "Round ${howMuchRoundCounter + 1} / $howMuchRoundValue"
        vm.setIsRoundTimeRunning(ROUND_TIME_STATE)
        startTimer()
    }

    private fun observeTimer() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.roundTimeState.combine(vm.currentTime) { state, timeTicking ->
                GenericPair(state, timeTicking)
            }.onEach {
                roundState = it.data1

                when (it.data1) {
                    ROUND_TIME_STATE -> {
                        it.data2?.let { timeTicking ->
                            val value = DateUtils.formatElapsedTime(timeTicking)
                            if (warningValue != null) {
                                if (value == "00:$warningValue") {
                                    vm.warningBellSound()
                                }
                            }
                            with(binding) {
                                isRest = false
                                timerSet = value
                            }
                        }
                    }
                    REST_TIME_STATE -> {
                        it.data2?.let { timeTicking ->
                            val value = DateUtils.formatElapsedTime(timeTicking)
                            if (value != "00:00") {
                                with(binding) {
                                    isRest = true
                                    timerSet = value
                                }
                            }
                        }
                    }
                }
            }.launchIn(this)
        }
    }

    private fun FragmentTimerBinding.disableView(isRunning: Boolean) {
        radioOff.isEnabled = !isRunning
        radioTenSec.isEnabled = !isRunning
        radioThirtySec.isEnabled = !isRunning
    }

    private fun FragmentTimerBinding.initNumberPicker() {
        npRestTime.apply {
            minValue = DEFAULT_INTEGER_VALUE
            maxValue = listRestTime.size - 1
            displayedValues = listRestTime
        }

        npRoundTime.apply {
            minValue = DEFAULT_INTEGER_VALUE
            maxValue = listRoundTime.size - 1
            displayedValues = listRoundTime
        }
        npWhichRound.apply {
            minValue = DEFAULT_INTEGER_VALUE
            maxValue = listWhichRound.size - 1
            displayedValues = listWhichRound
        }
    }

    private fun FragmentTimerBinding.getNumberPicker(){
        with(npRestTime){
            if (value == DEFAULT_INTEGER_VALUE) {
                vm.setRestTime(TimerConstant.setCustomTime(0))
            }
            setOnValueChangedListener { _, _, newVal ->
                when (newVal) {
                    0 -> vm.setRestTime(TimerConstant.setCustomTime(0))
                    1 -> vm.setRestTime(TimerConstant.setCustomTime(15))
                    2 -> vm.setRestTime(TimerConstant.setCustomTime(30))
                    3 -> vm.setRestTime(TimerConstant.setCustomTime(60))
                    4 -> vm.setRestTime(TimerConstant.setCustomTime(90))
                    5 -> vm.setRestTime(TimerConstant.setCustomTime(120))
                    6 -> vm.setRestTime(TimerConstant.setCustomTime(150))
                    7 -> vm.setRestTime(TimerConstant.setCustomTime(180))
                }
            }
        }
        with(npRoundTime){
            vm.setRoundTime(value)
            setOnValueChangedListener { _, _, newVal ->
                vm.setRoundTime(newVal)
            }
        }
        with(npWhichRound){
            vm.setWhichRound(value + 1)
            setOnValueChangedListener { _, _, newVal ->
                vm.setWhichRound(newVal + 1)
            }
        }
    }

    private fun FragmentTimerBinding.initRadioButton() {
        radioGroupRoot.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioOff -> vm.setWarningValue(0)
                R.id.radioTenSec -> vm.setWarningValue(10)
                R.id.radioThirtySec -> vm.setWarningValue(30)
            }
        }
    }

    private fun FragmentTimerBinding.inflateAdsView() {
        MobileAds.initialize(requireContext()) {
            Timber.e("init ads!")
        }
        val request = AdRequest.Builder().build()
        detailAdView.loadAd(request)
    }


}