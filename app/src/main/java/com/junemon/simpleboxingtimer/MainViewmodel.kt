package com.junemon.simpleboxingtimer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.junemon.simpleboxingtimer.util.TimerConstant
import com.junemon.simpleboxingtimer.util.TimerConstant.DONE
import com.junemon.simpleboxingtimer.util.TimerConstant.ONE_SECOND
import com.junemon.simpleboxingtimer.util.TimerConstant.ROUND_TIME_STATE
import com.junemon.simpleboxingtimer.util.ringer.BellRinger
import com.junemon.simpleboxingtimer.util.timer.BoxingTimer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * Created by Ian Damping on 18,October,2019
 * Github https://github.com/iandamping
 * Indonesia.
 */
@HiltViewModel
class MainViewmodel @Inject constructor(
    private val bellRinger: BellRinger,
    private val boxingTimer: BoxingTimer
) :
    ViewModel() {

    private val _isTimerRunning: MutableLiveData<Boolean> = MutableLiveData()
    private val _restTimeValue: MutableLiveData<Long> = MutableLiveData()
    private val _roundTimeValue: MutableLiveData<Long> = MutableLiveData()
    private val _whichRoundValue: MutableLiveData<Int> = MutableLiveData()
    private val _warningValue: MutableLiveData<Int> = MutableLiveData()
    private val _currentTime: MutableStateFlow<Long?> = MutableStateFlow(null)
    private val _pausedTime: MutableLiveData<Long> = MutableLiveData()
    private val _roundTimeState: MutableStateFlow<Int> = MutableStateFlow(ROUND_TIME_STATE)

    val roundTimeState: StateFlow<Int>
        get() = _roundTimeState

    val warningValue: LiveData<Int>
        get() = _warningValue

    val currentTime: StateFlow<Long?>
        get() = _currentTime

    val pausedTime: LiveData<Long>
        get() = _pausedTime

    val isTimerRunning: MutableLiveData<Boolean>
        get() = _isTimerRunning

    val restTimeValue: LiveData<Long>
        get() = _restTimeValue

    val roundTimeValue: LiveData<Long>
        get() = _roundTimeValue

    val whichRoundValue: LiveData<Int>
        get() = _whichRoundValue

    fun startTimer(durationTime: Long, finishTicking: () -> Unit) {
        boxingTimer.startTimer(durationTime = durationTime,
            onFinish = {
            _currentTime.value = DONE
            _pausedTime.value = DONE
            finishTicking.invoke()
        }, onTicking = { millisUntilFinished ->
            _currentTime.value = (millisUntilFinished / ONE_SECOND)
            _pausedTime.value = (millisUntilFinished / ONE_SECOND)
        })
    }

    fun cancelAllTimer() {
        boxingTimer.stopTimer()
    }

    fun setRestTime(data: Long) {
        _restTimeValue.value = data
    }

    fun setRoundTime(data: Int) {
        _roundTimeValue.value = when (data) {
            0 -> TimerConstant.setCustomTime(30)
            1 -> TimerConstant.setCustomMinutes(1)
            2 -> TimerConstant.setCustomMinutes(2)
            3 -> TimerConstant.setCustomMinutes(3)
            4 -> TimerConstant.setCustomMinutes(4)
            5 -> TimerConstant.setCustomMinutes(5)
            6 -> TimerConstant.setCustomMinutes(6)
            7 -> TimerConstant.setCustomMinutes(7)
            8 -> TimerConstant.setCustomMinutes(8)
            9 -> TimerConstant.setCustomMinutes(9)
            else -> TimerConstant.setCustomMinutes(10)
        }
    }

    fun setWhichRound(data: Int) {
        _whichRoundValue.value = data
    }

    fun setWarningValue(data: Int) {
        _warningValue.value = data
    }

    fun setTimmerIsRunning(data: Boolean) {
        _isTimerRunning.value = data
    }

    fun setIsRoundTimeRunning(data: Int) {
        _roundTimeState.value = data
    }

    fun startBellSound() {
        bellRinger.startBellSound()
    }

    fun endBellSound() {
        bellRinger.endBellSound()
    }

    fun warningBellSound() {
        bellRinger.warningBellSound()
    }

    override fun onCleared() {
        super.onCleared()
        cancelAllTimer()
    }
}