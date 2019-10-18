package com.junemon.simpleboxingtimer

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ian.app.helper.model.GenericViewModelZipperTriple
import com.junemon.simpleboxingtimer.TimerConstant.DONE
import com.junemon.simpleboxingtimer.TimerConstant.ONE_SECOND


/**
 * Created by Ian Damping on 18,October,2019
 * Github https://github.com/iandamping
 * Indonesia.
 */
class MainViewmodel : BaseViewModel() {
    private lateinit var timer: CountDownTimer
    private val _restTimeValue: MutableLiveData<Int> = MutableLiveData()
    private val _roundTimeValue: MutableLiveData<Int> = MutableLiveData()
    private val _whichRoundValue: MutableLiveData<Int> = MutableLiveData()

    private val _warningValue: MutableLiveData<Int> = MutableLiveData()

    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    private val restTimeValue: LiveData<Int>
        get() = _restTimeValue

    private val roundTimeValue: LiveData<Int>
        get() = _roundTimeValue

    private val whichRoundValue: LiveData<Int>
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

    fun cancelTimer(){
        if (::timer.isInitialized) timer.cancel()
    }


    fun setRestTime(data: Int) {
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

    fun getNumberPickerData() = GenericViewModelZipperTriple(
        restTimeValue,
        roundTimeValue,
        whichRoundValue
    ).getGenericData()



    override fun onCleared() {
        super.onCleared()
        if (::timer.isInitialized) timer.cancel()
    }
}