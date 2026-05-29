package com.junemon.simpleboxingtimer.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.android.gms.ads.AdView
import com.junemon.simpleboxingtimer.model.TimerClassification
import com.junemon.simpleboxingtimer.model.WarningTime
import com.junemon.simpleboxingtimer.util.TimerConstant

@Composable
fun PortraitBoxingTimerScreen(
    modifier: Modifier = Modifier,
    adView: AdView,
    isTimerRunning: Boolean,
    isRadioButtonEnabled: Boolean,
    isResting: Boolean,
    pauseTime: Long,
    timeTicking: Long?,
    currentRound: Int,
    whichRoundRunning: Int,
    selectedWarningOption: WarningTime,
    warningTimes: List<WarningTime>,
    timerClassifications: List<TimerClassification>,
    setRestTime: (Int) -> Unit,
    setRoundTime: (Int) -> Unit,
    setWhichRound: (Int) -> Unit,
    setWarningValue: (Int) -> Unit,
    cancelAllTimer: () -> Unit,
    startCounting: () -> Unit,
    resetAllTimer: () -> Unit,
    onWarningOptionSelected: (WarningTime) -> Unit,
) {
    ConstraintLayout(modifier = modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        val (timerRef, selectingTimeAndRoundRef, warningButtonRef, intervalTimerButtonRef, bannerAddViewRef) = createRefs()
        val topGuideLine = createGuidelineFromTop(0.1f)
        val bottomGuideLine = createGuidelineFromBottom(0.2f)
        val centerVerticalGuideline = createGuidelineFromTop(0.5f)

        if (isTimerRunning) {
            TimerInformationSection(
                modifier = Modifier.constrainAs(timerRef) {
                    top.linkTo(topGuideLine)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    bottom.linkTo(centerVerticalGuideline)
                    width = Dimension.fillToConstraints
                },
                isResting = isResting,
                timeTickingForText = timeTicking,
                currentRound = currentRound,
                whichRoundRunning = whichRoundRunning
            )
        } else {
            if (pauseTime != TimerConstant.DEFAULT_LONG_VALUE) {
                TimerInformationSection(
                    modifier = Modifier.constrainAs(timerRef) {
                        top.linkTo(topGuideLine)
                        bottom.linkTo(centerVerticalGuideline)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        width = Dimension.fillToConstraints
                    }, isResting = isResting,
                    timeTickingForText = timeTicking,
                    currentRound = currentRound,
                    whichRoundRunning = whichRoundRunning
                )
            } else IntervalSetupSection(
                modifier = Modifier.constrainAs(
                    selectingTimeAndRoundRef
                ) {
                    top.linkTo(topGuideLine)
                    bottom.linkTo(centerVerticalGuideline)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    width = Dimension.fillToConstraints
                },
                setRestTime = setRestTime,
                setRoundTime = setRoundTime,
                setWhichRound = setWhichRound,
                timerClassifications = timerClassifications
            )
        }

        WarningTimeRadioSection(
            modifier = Modifier.constrainAs(warningButtonRef) {
                top.linkTo(centerVerticalGuideline)
                bottom.linkTo(bottomGuideLine)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
            pauseTime = pauseTime,
            isRadioButtonEnabled = isRadioButtonEnabled,
            setWarningValue = setWarningValue,
            warningTimes = warningTimes,
            selectedOption = selectedWarningOption,
            onOptionSelected = onWarningOptionSelected,
        )

        IntervalTimerButtonSection(
            modifier = Modifier.constrainAs(intervalTimerButtonRef) {
                top.linkTo(warningButtonRef.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
            isTimerRunning = isTimerRunning,
            cancelAllTimer = cancelAllTimer,
            startCounting = startCounting,
            resetAll = resetAllTimer
        )

        AdsBannerScreen(adView, modifier = Modifier.constrainAs(bannerAddViewRef) {
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })
    }
}