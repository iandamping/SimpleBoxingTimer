package com.junemon.simpleboxingtimer.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface BoxingTimerNavigationKey : NavKey {
    @Serializable
    data object Home : BoxingTimerNavigationKey

    @Serializable
    data object Preset : BoxingTimerNavigationKey

    @Serializable
    data object CreatePreset : BoxingTimerNavigationKey

//    @Serializable
//    data class Detail(val itemId: String) : Screen
}
