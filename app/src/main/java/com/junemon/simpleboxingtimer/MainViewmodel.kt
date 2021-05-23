package com.junemon.simpleboxingtimer

import android.app.Application
import android.media.MediaPlayer
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.junemon.simpleboxingtimer.util.TimerConstant
import com.junemon.simpleboxingtimer.util.TimerConstant.DONE
import com.junemon.simpleboxingtimer.util.TimerConstant.ONE_SECOND
import com.junemon.simpleboxingtimer.util.TimerConstant.ROUND_TIME_STATE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Created by Ian Damping on 18,October,2019
 * Github https://github.com/iandamping
 * Indonesia.
 */
class MainViewmodel(private val applications: Application) : AndroidViewModel(applications) {
    private lateinit var timer: CountDownTimer

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
        timer = object : CountDownTimer(durationTime, ONE_SECOND) {
            override fun onFinish() {
                _currentTime.value = DONE
                _pausedTime.value = DONE
                finishTicking.invoke()
            }

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished / ONE_SECOND)
                _pausedTime.value = (millisUntilFinished / ONE_SECOND)
            }
        }.start()
    }

    fun cancelAllTimer() {
        if (::timer.isInitialized) {
            timer.cancel()
        }
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
        viewModelScope.launch {
            MediaPlayer.create(applications.applicationContext, R.raw.start_round_bell).start()
        }
    }

    fun endBellSound() {
        viewModelScope.launch {
            MediaPlayer.create(applications.applicationContext, R.raw.end_round_bell).start()
        }
    }

    fun warningBellSound() {
        viewModelScope.launch {
            MediaPlayer.create(applications.applicationContext, R.raw.warning_sound).start()
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelAllTimer()
    }
}