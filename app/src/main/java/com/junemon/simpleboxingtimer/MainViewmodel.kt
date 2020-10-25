package com.junemon.simpleboxingtimer

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.junemon.simpleboxingtimer.TimerConstant.DONE
import com.junemon.simpleboxingtimer.TimerConstant.ONE_SECOND
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Created by Ian Damping on 18,October,2019
 * Github https://github.com/iandamping
 * Indonesia.
 */
class MainViewmodel(private val context:Context) : BaseViewModel() {
    private lateinit var timer: CountDownTimer
    private lateinit var restTimer: CountDownTimer

    private val _isTimerRunning: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _restTimeValue: MutableStateFlow<Long> = MutableStateFlow(0)
    private val _roundTimeValue: MutableStateFlow<Int> = MutableStateFlow(0)
    private val _whichRoundValue: MutableStateFlow<Int> = MutableStateFlow(0)
    private val _warningValue: MutableStateFlow<Int> = MutableStateFlow(0)
    private val _currentTime: MutableStateFlow<Long?> = MutableStateFlow(null)
    private val _currentRestTime: MutableStateFlow<Long?> = MutableStateFlow(null)


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


    private fun cancelAllTimer() {
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

    fun startBellSound() {
        vmScope.launch {
            MediaPlayer.create(context, R.raw.boxing_start).start()
        }
    }

    fun endBellSound() {
        vmScope.launch {
            MediaPlayer.create(context, R.raw.boxing_end).start()
        }
    }

    fun warningBellSound() {
        vmScope.launch {
            MediaPlayer.create(context, R.raw.warning_sound).start()
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelAllTimer()
    }
}