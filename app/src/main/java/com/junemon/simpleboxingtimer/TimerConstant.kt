package com.junemon.simpleboxingtimer


/**
 * Created by Ian Damping on 18,October,2019
 * Github https://github.com/iandamping
 * Indonesia.
 */

object TimerConstant {
     const val DONE = 0L

     const val ONE_SECOND = 1000L

    fun setCustomMinutes(data: Int) = data * 60 * 1000L

    fun setCustomSeconds(data: Int) = data % 60 * 1000L
}