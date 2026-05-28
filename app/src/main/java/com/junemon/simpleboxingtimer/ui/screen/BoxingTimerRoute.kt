package com.junemon.simpleboxingtimer.ui.screen

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.junemon.simpleboxingtimer.viewmodel.AdsViewModel
import com.junemon.simpleboxingtimer.viewmodel.BoxingTimerViewModel
import com.junemon.simpleboxingtimer.viewmodel.DataProviderViewModel

@Composable
fun BoxingTimerRoute(
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    timerVm: BoxingTimerViewModel = hiltViewModel(),
    dataVm: DataProviderViewModel = hiltViewModel(),
    adsVm: AdsViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current

    val isTimerRunning by timerVm.isTimerRunning.collectAsStateWithLifecycle()
    val pauseTime by timerVm.pausedTime.collectAsStateWithLifecycle()
    val isResting by timerVm.isResting.collectAsStateWithLifecycle()
    val timeTickingForText by timerVm.currentTime.collectAsStateWithLifecycle()
    val currentRound by timerVm.roundCounter.collectAsStateWithLifecycle()
    val whichRoundRunning by timerVm.whichRoundValue.collectAsStateWithLifecycle()
    val isRadioButtonEnabled by timerVm.isTimerRunning.collectAsStateWithLifecycle()
    val selectedWarningOption by timerVm.selectedWarningOption.collectAsStateWithLifecycle()


    LifecycleOwnerListener(
        lifecycleOwner = lifecycleOwner,
        cancelAllTimer = timerVm::cancelAllTimer
    )

    if (configuration.orientation == ORIENTATION_LANDSCAPE) {
        LandscapeBoxingTimerScreen(
            modifier = modifier,
            isTimerRunning = isTimerRunning,
            isRadioButtonEnabled = isRadioButtonEnabled,
            isResting = isResting,
            pauseTime = pauseTime,
            timeTicking = timeTickingForText,
            currentRound = currentRound,
            whichRoundRunning = whichRoundRunning,
            warningTimes = dataVm.listOfWarningTimes,
            timerClassifications = dataVm.listOfTimerClassification,
            setRestTime = timerVm::setRestTime,
            setRoundTime = timerVm::setRoundTime,
            setWhichRound = timerVm::setWhichRound,
            setWarningValue = timerVm::setWarningValue,
            cancelAllTimer = timerVm::cancelAllTimer,
            startCounting = timerVm::startCounting,
            resetAllTimer = timerVm::resetAll,
            adView = adsVm.bannerAdView,
            selectedWarningOption = selectedWarningOption,
            onWarningOptionSelected = timerVm::setWarningOptions
        )
    } else {
        PortraitBoxingTimerScreen(
            modifier = modifier,
            isTimerRunning = isTimerRunning,
            isRadioButtonEnabled = isRadioButtonEnabled,
            isResting = isResting,
            pauseTime = pauseTime,
            timeTicking = timeTickingForText,
            currentRound = currentRound,
            whichRoundRunning = whichRoundRunning,
            warningTimes = dataVm.listOfWarningTimes,
            timerClassifications = dataVm.listOfTimerClassification,
            setRestTime = timerVm::setRestTime,
            setRoundTime = timerVm::setRoundTime,
            setWhichRound = timerVm::setWhichRound,
            setWarningValue = timerVm::setWarningValue,
            cancelAllTimer = timerVm::cancelAllTimer,
            startCounting = timerVm::startCounting,
            resetAllTimer = timerVm::resetAll,
            adView = adsVm.bannerAdView,
            selectedWarningOption = selectedWarningOption,
            onWarningOptionSelected = timerVm::setWarningOptions
        )
    }


}

@Composable
private fun LifecycleOwnerListener(
    lifecycleOwner: LifecycleOwner,
    cancelAllTimer: () -> Unit
) {
    DisposableEffect(lifecycleOwner) {
        // Create an observer that triggers our remembered callbacks
        // for sending analytics events
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                cancelAllTimer.invoke()
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}


