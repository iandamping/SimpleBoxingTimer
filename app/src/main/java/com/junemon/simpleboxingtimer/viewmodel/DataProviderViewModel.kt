package com.junemon.simpleboxingtimer.viewmodel

import androidx.lifecycle.ViewModel
import com.junemon.simpleboxingtimer.R
import com.junemon.simpleboxingtimer.util.resource.ResourceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DataProviderViewModel @Inject constructor(resourceHelper: ResourceHelper) :
    ViewModel() {

    val listOfWarningTimes = listOf(
        WarningTime(resourceHelper.provideString(resourceId = R.string.off), 0),
        WarningTime(resourceHelper.provideString(resourceId = R.string.ten_second), 10),
        WarningTime(resourceHelper.provideString(resourceId = R.string.thirty_sec), 30),
    )

    val listOfTimerClassification = listOf<TimerClassification>(
        TimerClassification(
            id = 0,
            title = resourceHelper.provideString(resourceId = R.string.rest),
            timerValues = resourceHelper.provideArrayOfString(resourceId = R.array.arr_rest_time)
                .toList(),
        ),
        TimerClassification(
            id = 1,
            title = resourceHelper.provideString(resourceId = R.string.round_time),
            timerValues = resourceHelper.provideArrayOfString(resourceId = R.array.arr_round_time)
                .toList(),
        ),
        TimerClassification(
            id = 2,
            title = resourceHelper.provideString(resourceId = R.string.rounds),
            timerValues = resourceHelper.provideArrayOfString(resourceId = R.array.arr_rounds)
                .toList(),
        ),
    )
}