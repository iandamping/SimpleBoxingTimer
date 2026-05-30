package com.junemon.simpleboxingtimer.util.resource

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ResourceHelperImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ResourceHelper {
    override fun provideString(resourceId: Int): String {
        return context.resources.getString(resourceId)
    }

    override fun provideArrayOfString(resourceId: Int): Array<String> {
        return context.resources.getStringArray(resourceId)
    }

    override fun provideArrayOfInteger(resourceId: Int): IntArray {
        return context.resources.getIntArray(resourceId)
    }
}