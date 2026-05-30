package com.junemon.simpleboxingtimer.navigation

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.junemon.simpleboxingtimer.ui.screen.BoxingTimerRoute
import com.junemon.simpleboxingtimer.ui.screen.preset.PresetRoute

@Composable
fun BoxingTimerNavigation(modifier: Modifier = Modifier) {

    val backStack =
        remember { mutableStateListOf<BoxingTimerNavigationKey>(BoxingTimerNavigationKey.Home) }
    val navItems = listOf(
        NavigationItem(BoxingTimerNavigationKey.Home, "Home", Icons.Default.Home),
        NavigationItem(BoxingTimerNavigationKey.Preset, "Presets", Icons.Default.Insights)
    )

    val currentDestination = backStack.lastOrNull() ?: BoxingTimerNavigationKey.Home

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == ORIENTATION_LANDSCAPE

    Row(modifier = modifier.fillMaxSize()) {
        if (isLandscape) {
            NavigationRail {
                navItems.forEach { item ->
                    NavigationRailItem(
                        selected = currentDestination == item.navigationKey,
                        onClick = {
                            navigateToTab(backStack, item.navigationKey)
                        },
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) }
                    )
                }
            }
        }

        Scaffold(
            bottomBar = {
                if (!isLandscape) {
                    NavigationBar {
                        navItems.forEach { item ->
                            NavigationBarItem(
                                selected = currentDestination == item.navigationKey,
                                onClick = {
                                    navigateToTab(backStack, item.navigationKey)
                                },
                                icon = { Icon(item.icon, contentDescription = item.title) },
                                label = { Text(item.title) }
                            )
                        }
                    }
                }
            }
        ) { innerPading ->
            NavDisplay(
                backStack = backStack,
                modifier = Modifier.padding(innerPading)
            ) { key ->
                when (key) {
                    is BoxingTimerNavigationKey.Home -> NavEntry(key) { BoxingTimerRoute() }

                    else -> NavEntry(key) {
                        PresetRoute(onCreateNewPreset = {})
                    }
                }
            }
        }
    }
}

fun navigateToTab(
    backStack: MutableList<BoxingTimerNavigationKey>,
    targetScreen: BoxingTimerNavigationKey
) {
    if (backStack.lastOrNull() == targetScreen) return

    if (backStack.contains(targetScreen)) {
        val index = backStack.indexOf(targetScreen)
        while (backStack.size > index + 1) {
            backStack.removeAt(backStack.lastIndex)
        }
    } else {
        val root = backStack.firstOrNull() ?: BoxingTimerNavigationKey.Home
        backStack.clear()
        backStack.add(root)

        if (targetScreen != root) {
            backStack.add(targetScreen)
        }
    }
}