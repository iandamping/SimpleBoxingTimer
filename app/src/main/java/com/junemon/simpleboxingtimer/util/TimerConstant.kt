package com.junemon.simpleboxingtimer.util

import android.view.View

/**
 * Created by Ian Damping on 18,October,2019
 * Github https://github.com/iandamping
 * Indonesia.
 */

object TimerConstant {
    const val MY_REQUEST_CODE: Int = 0
    const val DEFAULT_ROUND_COUNTER_VALUE = 1
    const val DEFAULT_INTEGER_VALUE = 0
    const val DEFAULT_WHICH_ROUND_COUNTER_VALUE = 1
    const val DEFAULT_LONG_VALUE = 0L
    const val ROUND_TIME_STATE = 0
    const val REST_TIME_STATE = 1

    const val DONE = 0L
    const val DONE_FLOAT = 0F

    const val ONE_SECOND = 1000L

    fun setCustomHour(data: Int) = data * 3600 * 1000L

    fun setCustomMinutes(data: Int) = data * 60 * 1000L

    fun setCustomSeconds(data: Int) = data * 1000L

    fun setCustomTime(data: Int) = data * 1000L

}