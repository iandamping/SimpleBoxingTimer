package com.junemon.simpleboxingtimer.util.resource

import androidx.annotation.ArrayRes
import androidx.annotation.StringRes

interface ResourceHelper {
    fun provideString(@StringRes resourceId: Int): String

    fun provideArrayOfString(@ArrayRes resourceId: Int): Array<String>
}