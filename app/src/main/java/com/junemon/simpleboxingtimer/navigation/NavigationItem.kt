package com.junemon.simpleboxingtimer.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val navigationKey: BoxingTimerNavigationKey,
    val title: String,
    val icon: ImageVector
)
