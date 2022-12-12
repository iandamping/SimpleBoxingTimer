package com.junemon.simpleboxingtimer.ui.screen

import android.content.pm.ActivityInfo
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.junemon.simpleboxingtimer.R
import com.junemon.simpleboxingtimer.util.TimerConstant
import com.junemon.simpleboxingtimer.viewmodel.BoxingTimerViewModel

@Composable
fun BoxingTimerScreen(
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    timerVm: BoxingTimerViewModel
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    val isTimerRunning by timerVm.isTimerRunning.observeAsState(initial = false)
    val pauseTime by timerVm.pausedTime.observeAsState()
    val isResting by timerVm.isResting.observeAsState(initial = false)
    val timeTickingForText by timerVm.currentTime.collectAsState()
    val currentRound by timerVm.roundCounter.observeAsState(initial = TimerConstant.DEFAULT_ROUND_COUNTER_VALUE)
    val whichRoundRunning by timerVm.whichRoundValue.observeAsState(initial = TimerConstant.DEFAULT_ROUND_COUNTER_VALUE)
    val isRadioButtonEnabled by timerVm.isTimerRunning.observeAsState(initial = false)

    DisposableEffect(lifecycleOwner) {
        // Create an observer that triggers our remembered callbacks
        // for sending analytics events
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                timerVm.cancelAllTimer()
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    ConstraintLayout(modifier = modifier.verticalScroll(rememberScrollState())) {
        val (timerRef, selectingTimeAndRoundRef, warningButtonRef, intervalTimerButtonRef, bannerAddViewRef) = createRefs()
        val topGuideLine = createGuidelineFromTop(0.1f)
        val bottomGuideLine = createGuidelineFromBottom(0.2f)
        if (isTimerRunning) {
            TimerText(
                modifier = Modifier.constrainAs(timerRef) {
                    top.linkTo(topGuideLine)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    width = Dimension.fillToConstraints
                },
                isResting = isResting,
                timeTickingForText = timeTickingForText,
                currentRound = currentRound,
                whichRoundRunning = whichRoundRunning
            )
        } else {
            if (pauseTime != TimerConstant.DEFAULT_LONG_VALUE) {
                TimerText(
                    modifier = Modifier.constrainAs(timerRef) {
                        top.linkTo(topGuideLine)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        width = Dimension.fillToConstraints
                    }, isResting = isResting,
                    timeTickingForText = timeTickingForText,
                    currentRound = currentRound,
                    whichRoundRunning = whichRoundRunning
                )
            } else NumberPickerSelectingRestRoundTimeAndRoundScreen(
                modifier = Modifier.constrainAs(
                    selectingTimeAndRoundRef
                ) {
                    top.linkTo(topGuideLine)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    width = Dimension.fillToConstraints
                },
                setRestTime = timerVm::setRestTime,
                setRoundTime = timerVm::setRoundTime,
                setWhichRound = timerVm::setWhichRound
            )
        }

        WarningTimeRadioButton(
            modifier = Modifier.constrainAs(warningButtonRef) {
                top.linkTo(
                    if (isTimerRunning) {
                        timerRef.bottom
                    } else {
                        if (pauseTime != TimerConstant.DEFAULT_LONG_VALUE) {
                            timerRef.bottom
                        } else selectingTimeAndRoundRef.bottom
                    }
                )
                bottom.linkTo(bottomGuideLine)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
            pauseTime = pauseTime,
            isRadioButtonEnabled = isRadioButtonEnabled,
            setWarningValue = timerVm::setWarningValue
        )

        IntervalTimerButton(
            modifier = Modifier.constrainAs(intervalTimerButtonRef) {
                top.linkTo(warningButtonRef.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
            isTimerRunning = isTimerRunning,
            cancelAllTimer = timerVm::cancelAllTimer,
            startCounting = timerVm::startCounting,
            resetAll = timerVm::resetAll
        )

        AndroidView(modifier = Modifier.constrainAs(bannerAddViewRef) {
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
        }, factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = context.getString(R.string.admob_unit_id_banner)
                loadAd(AdRequest.Builder().build())
            }
        })
    }
}


