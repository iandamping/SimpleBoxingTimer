package com.junemon.simpleboxingtimer.viewmodel

import android.text.format.DateUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junemon.simpleboxingtimer.R
import com.junemon.simpleboxingtimer.util.TimerConstant
import com.junemon.simpleboxingtimer.util.TimerConstant.DEFAULT_INTEGER_VALUE
import com.junemon.simpleboxingtimer.util.TimerConstant.DEFAULT_LONG_VALUE
import com.junemon.simpleboxingtimer.util.TimerConstant.DEFAULT_ROUND_COUNTER_VALUE
import com.junemon.simpleboxingtimer.util.TimerConstant.DEFAULT_WHICH_ROUND_COUNTER_VALUE
import com.junemon.simpleboxingtimer.util.TimerConstant.DONE
import com.junemon.simpleboxingtimer.util.TimerConstant.ONE_SECOND
import com.junemon.simpleboxingtimer.util.TimerConstant.REST_TIME_STATE
import com.junemon.simpleboxingtimer.util.TimerConstant.ROUND_TIME_STATE
import com.junemon.simpleboxingtimer.util.TimerConstant.setCustomMinutes
import com.junemon.simpleboxingtimer.util.resource.ResourceHelper
import com.junemon.simpleboxingtimer.util.ringer.BellRinger
import com.junemon.simpleboxingtimer.util.timer.BoxingTimer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoxingTimerViewModel @Inject constructor(
    private val timerHelper: BoxingTimer,
    private val bellRinger: BellRinger,
    private val resourceHelper: ResourceHelper
) : ViewModel() {

    private val _isTimerRunning: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> get() = _isTimerRunning.asStateFlow()

    private val _restTimeValue: MutableStateFlow<Int> = MutableStateFlow(DEFAULT_INTEGER_VALUE)
    val restTimeValue: StateFlow<Int> get() = _restTimeValue.asStateFlow()

    private val _roundTimeValue: MutableStateFlow<Int> = MutableStateFlow(DEFAULT_INTEGER_VALUE)
    val roundTimeValue: StateFlow<Int> get() = _roundTimeValue.asStateFlow()

    private val _whichRoundValue: MutableStateFlow<Int> = MutableStateFlow(DEFAULT_INTEGER_VALUE)
    val whichRoundValue: StateFlow<Int> get() = _whichRoundValue.asStateFlow()

    private val _warningValue: MutableStateFlow<Int> = MutableStateFlow(DEFAULT_INTEGER_VALUE)
    val warningValue: StateFlow<Int> get() = _warningValue.asStateFlow()

    private val _currentTime: MutableStateFlow<Long?> = MutableStateFlow(null)
    val currentTime: StateFlow<Long?> get() = _currentTime.asStateFlow()

    private val _pausedTime: MutableStateFlow<Long> = MutableStateFlow(DEFAULT_LONG_VALUE)
    val pausedTime: StateFlow<Long> get() = _pausedTime

    private val _roundTimeState: MutableStateFlow<Int> = MutableStateFlow(ROUND_TIME_STATE)
    val roundTimeState: StateFlow<Int> get() = _roundTimeState.asStateFlow()

    private var _isResting: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isResting: StateFlow<Boolean> get() = _isResting.asStateFlow()

    private var _roundCounter: MutableStateFlow<Int> = MutableStateFlow(DEFAULT_ROUND_COUNTER_VALUE)
    val roundCounter: StateFlow<Int> get() = _roundCounter.asStateFlow()

    private var _selectedWarningOption: MutableStateFlow<WarningTime> =
        MutableStateFlow(WarningTime(resourceHelper.provideString(resourceId = R.string.off), 0))
    val selectedWarningOption: StateFlow<WarningTime> get() = _selectedWarningOption.asStateFlow()


    init {
        observeTimer()
    }

    fun startCounting() {
        when (roundTimeState.value) {
            ROUND_TIME_STATE -> {
                if (pausedTime.value.toInt() != DEFAULT_INTEGER_VALUE) {
                    startTimer(pausedTime.value.times(ONE_SECOND)) {
                        setPauseTime(DONE)
                        when {
                            (roundCounter.value) < (whichRoundValue.value) -> {
                                startingTimerForRoundOnly()
                            }

                            else -> {
                                resetAll()
                                endBellSound()
                            }
                        }
                    }
                } else {
                    startBellSound()
                    startTimer(roundTimeMapper(roundTimeValue.value)) {
                        when {
                            (roundCounter.value) < (whichRoundValue.value) -> {
                                startingTimerForRoundOnly()
                            }

                            else -> {
                                resetAll()
                                endBellSound()
                            }
                        }
                    }
                }
            }

            REST_TIME_STATE -> {
                if (pausedTime.value.toInt() != DEFAULT_INTEGER_VALUE) {
                    startTimer(pausedTime.value.times(ONE_SECOND)) {
                        setPauseTime(DONE)
                        startingTimerForRestOnly()
                    }
                } else {
                    startTimer(restTimeMapper(restTimeValue.value)) {
                        startingTimerForRestOnly()
                    }
                }
            }
        }
    }

    private fun observeTimer() {
        viewModelScope.launch {
            combine(roundTimeState, currentTime, warningValue) { state, timeTicking, warning ->
                Triple(state, timeTicking, warning)
            }.collect {
                when (it.first) {
                    ROUND_TIME_STATE -> {
                        it.second?.let { timeTicking ->
                            val value = DateUtils.formatElapsedTime(timeTicking)
                            setResting(false)

                            if (it.third != DEFAULT_INTEGER_VALUE) {
                                if (value == "00:${it.third}") {
                                    warningBellSound()
                                }
                            }

                        }
                    }

                    REST_TIME_STATE -> {
                        it.second?.let { timeTicking ->
                            val value = DateUtils.formatElapsedTime(timeTicking)

                            if (value != "00:00") {
                                setResting(true)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun startTimer(
        durationTime: Long,
        finishTicking: () -> Unit
    ) {
        setTimerIsRunning(true)
        timerHelper.startTimer(
            durationTime = durationTime,
            onFinish = {
                setCurrentTime(DONE)
                setPauseTime(DONE)
                finishTicking.invoke()
            }, onTicking = { millisUntilFinished ->
                setCurrentTime((millisUntilFinished / ONE_SECOND))
                setPauseTime((millisUntilFinished / ONE_SECOND))
            })
    }

    private fun startingTimerForRoundOnly() {
        if ((restTimeValue.value) == DEFAULT_INTEGER_VALUE) {
            incrementRoundCounter()
            setIsRoundTimeRunning(ROUND_TIME_STATE)
            startCounting()
        } else {
            endBellSound()
            setIsRoundTimeRunning(REST_TIME_STATE)
            startCounting()
        }
    }

    private fun startingTimerForRestOnly() {
        incrementRoundCounter()
        setIsRoundTimeRunning(ROUND_TIME_STATE)
        startCounting()
    }

    fun setRoundTime(data: Int) {
        _roundTimeValue.update { data }
    }

    private fun resetRoundTime() {
        _roundTimeValue.update { DEFAULT_INTEGER_VALUE }
    }

    private fun roundTimeMapper(data: Int?): Long {
        return if (data != null) {
            setCustomMinutes(data + 1)
        } else {
            setCustomMinutes(1)
        }
    }

    private fun restTimeMapper(data: Int?): Long {
        return if (data != null) {
            when (data) {
                0 -> TimerConstant.setCustomTime(0)
                1 -> TimerConstant.setCustomTime(15)
                2 -> TimerConstant.setCustomTime(30)
                3 -> TimerConstant.setCustomTime(60)
                4 -> TimerConstant.setCustomTime(90)
                5 -> TimerConstant.setCustomTime(120)
                6 -> TimerConstant.setCustomTime(150)
                else -> TimerConstant.setCustomTime(180)
            }
        } else {
            TimerConstant.setCustomTime(0)
        }
    }

    fun resetAll() {
        resetRoundTime()
        resetRestTime()
        resetRoundCounter()
        resetPauseTime()
        resetResting()
        resetWhichRound()
        cancelAllTimer()
        setCurrentTime(DONE)
        setPauseTime(DONE)
        setTimerIsRunning(false)
        setIsRoundTimeRunning(ROUND_TIME_STATE)
        setWarningOptions(WarningTime(resourceHelper.provideString(resourceId = R.string.off), 0))
        resetWarningValue()
    }

    fun cancelAllTimer() {
        setTimerIsRunning(false)
        timerHelper.stopTimer()
    }

    private fun setCurrentTime(data: Long) {
        _currentTime.update { data }
    }

    private fun setPauseTime(data: Long) {
        _pausedTime.update { data }
    }

    private fun resetPauseTime() {
        _pausedTime.update { DEFAULT_LONG_VALUE }
    }

    fun setRestTime(data: Int) {
        _restTimeValue.update { data }
    }

    private fun resetRestTime() {
        _restTimeValue.update { DEFAULT_INTEGER_VALUE }
    }

    private fun incrementRoundCounter() {
        _roundCounter.update { (_roundCounter.value).plus(1) }
    }

    private fun resetRoundCounter() {
        _roundCounter.update { DEFAULT_ROUND_COUNTER_VALUE }
    }


    private fun setResting(data: Boolean) {
        _isResting.update { data }
    }

    private fun resetResting() {
        setResting(false)
    }

    fun setWhichRound(data: Int) {
        if (data == 16) {
            //set infinity
            _whichRoundValue.update { Int.MAX_VALUE }
        } else _whichRoundValue.update { data }
    }

    fun setWarningOptions(time: WarningTime) {
        _selectedWarningOption.update { time }
    }

    private fun resetWhichRound() {
        _whichRoundValue.update { DEFAULT_WHICH_ROUND_COUNTER_VALUE }
    }

    fun setWarningValue(data: Int) {
        _warningValue.update { data }
    }

    private fun resetWarningValue() {
        _warningValue.update { DEFAULT_INTEGER_VALUE }
    }

    private fun setTimerIsRunning(data: Boolean) {
        _isTimerRunning.update { data }
    }

    private fun setIsRoundTimeRunning(data: Int) {
        _roundTimeState.update { data }
    }

    private fun startBellSound() {
        bellRinger.startBellSound()
    }

    private fun endBellSound() {
        bellRinger.endBellSound()
    }

    private fun warningBellSound() {
        bellRinger.warningBellSound()
    }

    override fun onCleared() {
        super.onCleared()
        cancelAllTimer()
    }

}