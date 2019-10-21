package com.junemon.simpleboxingtimer

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ian.app.helper.model.GenericViewModelZipperTriple
import com.junemon.simpleboxingtimer.TimerConstant.DONE
import com.junemon.simpleboxingtimer.TimerConstant.ONE_SECOND
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * Created by Ian Damping on 18,October,2019
 * Github https://github.com/iandamping
 * Indonesia.
 */
class MainViewmodel : BaseViewModel() {
    private lateinit var timer: CountDownTimer
    private lateinit var restTimer: CountDownTimer
    private val _restTimeValue: MutableLiveData<Long> = MutableLiveData()
    private val _roundTimeValue: MutableLiveData<Int> = MutableLiveData()
    private val _whichRoundValue: MutableLiveData<Int> = MutableLiveData()

    private val _warningValue: MutableLiveData<Int> = MutableLiveData()

    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    private val _currentRestTime = MutableLiveData<Long>()
    val currentRestTime: LiveData<Long>
        get() = _currentRestTime


     val restTimeValue: LiveData<Long>
        get() = _restTimeValue

    val roundTimeValue: LiveData<Int>
        get() = _roundTimeValue

     val whichRoundValue: LiveData<Int>
        get() = _whichRoundValue

    val warningValue: LiveData<Int>
        get() = _warningValue

    fun startTimer(durationTime: Long) {
        timer = object : CountDownTimer(durationTime, ONE_SECOND) {
            override fun onFinish() {
                _currentTime.value = DONE
            }

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished / ONE_SECOND)
            }
        }
        if (::timer.isInitialized) timer.start()
    }

    fun startRestTimer(durationTime: Long) {
        restTimer = object : CountDownTimer(durationTime, ONE_SECOND) {
            override fun onFinish() {
                _currentRestTime.value = DONE
            }

            override fun onTick(millisUntilFinished: Long) {
                _currentRestTime.value = (millisUntilFinished / ONE_SECOND)
            }
        }
        if (::restTimer.isInitialized) restTimer.start()
    }


    fun runningDelay(ctx: Context, sequence: Int, firstDurationTime: Long, secondDurationTime: Long, func: () -> Unit, restTime: () -> Unit) {
        vmScope.launch {
            var x = 0
            while (x < sequence) {
                //round time
                func.invoke()
                startBellSound(ctx)

                //rest time
                delay(firstDurationTime)
                restTime.invoke()
//                startBellSound(ctx)

                //run loop again
                delay(secondDurationTime)
                x++
            }
            if (x == sequence) {
                cancelAllTimer()
                endBellSound(ctx)
            }
        }
    }


    private fun cancelAllTimer() {
        cancelTimer()
        cancelRestTimer()
    }



    fun cancelTimer() {
        if (::timer.isInitialized) timer.cancel()
    }

    fun cancelRestTimer() {
        if (::restTimer.isInitialized) restTimer.start()
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

    fun getNumberPickerData() = GenericViewModelZipperTriple(restTimeValue, roundTimeValue, whichRoundValue).getGenericData()

    private fun startBellSound(ctx: Context) {
        vmScope.launch {
            MediaPlayer.create(ctx, R.raw.boxing_start).start()
        }
    }

    private fun endBellSound(ctx: Context) {
        vmScope.launch {
            MediaPlayer.create(ctx, R.raw.boxing_end).start()
        }
    }

    fun warningBellSound(ctx: Context) {
        vmScope.launch {
            MediaPlayer.create(ctx, R.raw.boxing_warning).start()
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (::timer.isInitialized) timer.cancel()
        if (::restTimer.isInitialized) restTimer.start()
    }
}