package com.junemon.simpleboxingtimer.util

import android.view.View

/**
 * Created by Ian Damping on 18,October,2019
 * Github https://github.com/iandamping
 * Indonesia.
 */

object TimerConstant {
    const val MY_REQUEST_CODE: Int = 0
    const val IMMERSIVE_FLAG_TIMEOUT = 500L

    const val DEFAULT_INTEGER_VALUE = 0
    const val DEFAULT_LONG_VALUE = 0L
    const val ROUND_TIME_STATE = 0
    const val REST_TIME_STATE = 1

     const val DONE = 0L

     const val ONE_SECOND = 1000L

    fun setCustomMinutes(data: Int) = data * 60 * 1000L

    fun setCustomSeconds(data: Int) = data % 60 * 1000L

    fun setCustomTime(data:Int) = data * 1000L

    /** Combination of all flags required to put activity into immersive mode */
    const val FLAGS_FULLSCREEN =
        View.SYSTEM_UI_FLAG_LOW_PROFILE or
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
}