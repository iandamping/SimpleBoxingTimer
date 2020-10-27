package com.junemon.simpleboxingtimer

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junemon.simpleboxingtimer.util.TimerConstant.DONE
import com.junemon.simpleboxingtimer.util.TimerConstant.ONE_SECOND
import com.junemon.simpleboxingtimer.util.TimerConstant.ROUND_TIME_STATE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Created by Ian Damping on 18,October,2019
 * Github https://github.com/iandamping
 * Indonesia.
 */
@ExperimentalCoroutinesApi
class MainViewmodel(private val context:Context) : ViewModel() {
    private lateinit var timer: CountDownTimer
    private lateinit var restTimer: CountDownTimer

    private val _isTimerRunning: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _restTimeValue: MutableStateFlow<Long> = MutableStateFlow(0)
    private val _roundTimeValue: MutableStateFlow<Int> = MutableStateFlow(0)
    private val _whichRoundValue: MutableStateFlow<Int> = MutableStateFlow(0)
    private val _warningValue: MutableStateFlow<Int> = MutableStateFlow(0)
    private val _currentTime: MutableStateFlow<Long?> = MutableStateFlow(null)
    private val _currentRestTime: MutableStateFlow<Long?> = MutableStateFlow(null)

    private val _roundTimeState: MutableStateFlow<Int> = MutableStateFlow(ROUND_TIME_STATE)

    val roundTimeState: StateFlow<Int>
        get() = _roundTimeState

    val warningValue: StateFlow<Int>
        get() = _warningValue

    val currentTime: StateFlow<Long?>
        get() = _currentTime

    val currentRestTime: StateFlow<Long?>
        get() = _currentRestTime

    val isTimerRunning: MutableStateFlow<Boolean>
        get() = _isTimerRunning

    val restTimeValue: StateFlow<Long>
        get() = _restTimeValue

    val roundTimeValue: StateFlow<Int>
        get() = _roundTimeValue

    val whichRoundValue: StateFlow<Int>
        get() = _whichRoundValue

    fun startTimer(durationTime: Long,finishTicking:()->Unit) {
        timer = object : CountDownTimer(durationTime, ONE_SECOND) {
            override fun onFinish() {
                _currentTime.value = DONE
                finishTicking.invoke()
            }

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished / ONE_SECOND)
            }
        }.start()
    }

    fun startRestTimer(durationTime: Long,finishTicking:()->Unit) {
        restTimer = object : CountDownTimer(durationTime, ONE_SECOND) {
            override fun onFinish() {
                _currentRestTime.value = DONE
                finishTicking.invoke()
            }

            override fun onTick(millisUntilFinished: Long) {
                _currentRestTime.value = (millisUntilFinished / ONE_SECOND)
            }
        }.start()
    }


    fun cancelAllTimer() {
        if (::timer.isInitialized) {
            timer.cancel()
        }
        if (::restTimer.isInitialized) {
            restTimer.cancel()
        }
    }

    fun setRestTime(data: Long) {
        _restTimeValue.value = data
    }

    fun setRoundTime(data: Int) {
        _roundTimeValue.value = data
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
        viewModelScope.launch {
            MediaPlayer.create(context, R.raw.end_round_bell).start()
        }
    }

    fun endBellSound() {
        viewModelScope.launch {
            MediaPlayer.create(context, R.raw.end_round_bell).start()
        }
    }

    fun warningBellSound() {
        viewModelScope.launch {
            MediaPlayer.create(context, R.raw.warning_sound).start()
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelAllTimer()
    }
}