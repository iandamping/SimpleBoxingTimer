package com.junemon.simpleboxingtimer

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.junemon.simpleboxingtimer.ui.screen.BoxingTimerScreen
import com.junemon.simpleboxingtimer.ui.theme.DarkBlue900
import com.junemon.simpleboxingtimer.ui.theme.TimerTheme
import com.junemon.simpleboxingtimer.viewmodel.BoxingTimerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val boxingVm: BoxingTimerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContent {
            TimerTheme {
                BoxingTimerScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DarkBlue900),
                    timerVm = boxingVm
                )
            }
        }
    }

}

