package com.junemon.simpleboxingtimer.ui.screen

import android.text.format.DateUtils
import android.widget.NumberPicker
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.junemon.simpleboxingtimer.R
import com.junemon.simpleboxingtimer.ui.theme.CalculatorFontFamily
import com.junemon.simpleboxingtimer.util.TimerConstant
import com.junemon.simpleboxingtimer.util.TimerConstant.DEFAULT_INTEGER_VALUE
import com.junemon.simpleboxingtimer.viewmodel.BoxingTimerViewModel
import com.junemon.simpleboxingtimer.viewmodel.RestTime

@Composable
fun BoxingTimerScreen(modifier: Modifier = Modifier, timerVm: BoxingTimerViewModel) {
    val isTimerRunning by timerVm.isTimerRunning.observeAsState(initial = false)
    val pauseTime by timerVm.pausedTime.observeAsState()

    ConstraintLayout(modifier = modifier.verticalScroll(rememberScrollState())) {
        val (timerRef, selectingTimeAndRoundRef, warningButtonRef, intervalTimerButtonRef, bannerAddViewRef) = createRefs()
        val topGuideLine = createGuidelineFromTop(0.1f)
        val bottomGuideLine = createGuidelineFromBottom(0.2f)
        if (isTimerRunning) {
            TimerText(modifier = Modifier.constrainAs(timerRef) {
                top.linkTo(topGuideLine)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
                width = Dimension.fillToConstraints
            }, timerVm = timerVm)
        } else {
            if (pauseTime != TimerConstant.DEFAULT_LONG_VALUE) {
                TimerText(modifier = Modifier.constrainAs(timerRef) {
                    top.linkTo(topGuideLine)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    width = Dimension.fillToConstraints
                }, timerVm = timerVm)
            } else SelectingRestRoundTimeAndRoundScreen(
                modifier = Modifier.constrainAs(
                    selectingTimeAndRoundRef
                ) {
                    top.linkTo(topGuideLine)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    width = Dimension.fillToConstraints
                }, timerVm = timerVm
            )
        }

        WarningTimeRadioButton(modifier = Modifier.constrainAs(warningButtonRef) {
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
        }, timerVm = timerVm)

        IntervalTimerButton(modifier = Modifier.constrainAs(intervalTimerButtonRef) {
            top.linkTo(warningButtonRef.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
        }, timerVm = timerVm)

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


@Composable
private fun TimerText(modifier: Modifier = Modifier, timerVm: BoxingTimerViewModel) {
    val isResting by timerVm.isResting.observeAsState(initial = false)
    val timeTickingForText by timerVm.currentTime.collectAsState()
    val currentRound by timerVm.roundCounter.observeAsState(initial = TimerConstant.DEFAULT_ROUND_COUNTER_VALUE)
    val whichRoundRunning by timerVm.whichRoundValue.observeAsState(initial = TimerConstant.DEFAULT_ROUND_COUNTER_VALUE)
    Column(
        modifier = modifier
            .padding(8.dp)
            .height(200.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(
                if (whichRoundRunning == Int.MAX_VALUE) R.string.infinity_round else R.string.current_round,
                currentRound,
                whichRoundRunning
            ),
            style = MaterialTheme.typography.h4.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontFamily = CalculatorFontFamily,
                textAlign = TextAlign.Center
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = DateUtils.formatElapsedTime(timeTickingForText ?: 0L),
            style = MaterialTheme.typography.h1.copy(
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = CalculatorFontFamily,
                textAlign = TextAlign.Center
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        if (isResting) {
            Text(
                text = stringResource(id = R.string.rest),
                style = MaterialTheme.typography.h2.copy(
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = CalculatorFontFamily,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }

}

@Composable
fun WarningTimeRadioButton(
    modifier: Modifier = Modifier,
    timerVm: BoxingTimerViewModel
) {
    val listOfRestTime = listOf(
        RestTime(stringResource(id = R.string.off), 0),
        RestTime(stringResource(id = R.string.ten_second), 10),
        RestTime(stringResource(id = R.string.thirty_sec), 30),
    )
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(listOfRestTime[0]) }
    val pauseTime by timerVm.pausedTime.observeAsState()
    val isRadioButtonEnabled by timerVm.isTimerRunning.observeAsState(initial = false)

    Column(
        modifier = modifier
            .padding(12.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = stringResource(id = R.string.warning),
            color = Color.White,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp, bottom = 8.dp)
        )

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOfRestTime.forEach { text ->
                Row(
                    Modifier
                        .wrapContentWidth()
                        .selectable(
                            // this method is called when
                            // radio button is selected.
                            selected = (text == selectedOption),
                            onClick = {
                                onOptionSelected(text)
                                timerVm.setWarningValue(text.time)
                            },
                            enabled = if (isRadioButtonEnabled) {
                                false
                            } else {
                                pauseTime == TimerConstant.DEFAULT_LONG_VALUE
                            }
                        )
                        // below line is use to add
                        // padding to radio button.
                        .padding(horizontal = 4.dp)

                ) {
                    // below line is use to
                    // generate radio button
                    RadioButton(
                        // inside this method we are
                        // adding selected with a option.
                        selected = (text == selectedOption),
                        onClick = {
                            // inide on click method we are setting a
                            // selected option of our radio buttons.
                            onOptionSelected(text)
                            timerVm.setWarningValue(text.time)
                        },
                        enabled = if (isRadioButtonEnabled) {
                            false
                        } else {
                            pauseTime == TimerConstant.DEFAULT_LONG_VALUE
                        }
                    )
                    // below line is use to add
                    // text to our radio buttons.
                    Text(
                        text = text.name,
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 2.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
                Spacer(modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@Composable
fun SelectingRestRoundTimeAndRoundScreen(
    modifier: Modifier = Modifier,
    timerVm: BoxingTimerViewModel
) {
    val restArray = stringArrayResource(id = R.array.arr_rest_time)
    val roundTimeArray = stringArrayResource(id = R.array.arr_round_time)
    val roundsArray = stringArrayResource(id = R.array.arr_rounds)
    val pickerTheme = R.style.AppTheme_Picker
    Row(
        modifier = modifier
            .padding(8.dp)
            .height(200.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(id = R.string.rest), color = Color.White)
            AndroidView(factory = {
                NumberPicker(ContextThemeWrapper(it, pickerTheme)).apply {
                    if (value == DEFAULT_INTEGER_VALUE) {
                        timerVm.setRestTime(DEFAULT_INTEGER_VALUE)
                    }
                    minValue = DEFAULT_INTEGER_VALUE
                    maxValue = restArray.size - 1
                    displayedValues = restArray
                    setOnValueChangedListener { _, _, newVal ->
                        timerVm.setRestTime(newVal)
                    }
                }
            })
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(id = R.string.round_time), color = Color.White)

            AndroidView(factory = {
                NumberPicker(ContextThemeWrapper(it, pickerTheme)).apply {
                    timerVm.setRoundTime(value)
                    minValue = DEFAULT_INTEGER_VALUE
                    maxValue = roundTimeArray.size - 1
                    displayedValues = roundTimeArray
                    setOnValueChangedListener { _, _, newVal ->
                        timerVm.setRoundTime(newVal)
                    }
                }
            })
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(id = R.string.rounds), color = Color.White)

            AndroidView(factory = {
                NumberPicker(ContextThemeWrapper(it, pickerTheme)).apply {
                    timerVm.setWhichRound(value + 1)
                    minValue = DEFAULT_INTEGER_VALUE
                    maxValue = roundsArray.size - 1
                    displayedValues = roundsArray
                    setOnValueChangedListener { _, _, newVal ->
                        timerVm.setWhichRound(newVal + 1)
                    }
                }
            })
        }
    }
}

@Composable
fun IntervalTimerButton(modifier: Modifier = Modifier, timerVm: BoxingTimerViewModel) {

    val isTimerRunning by timerVm.isTimerRunning.observeAsState(initial = false)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        Button(onClick = {
            if (isTimerRunning) {
                timerVm.cancelAllTimer()
            } else timerVm.startCounting()
        }) {
            Text(
                text = if (isTimerRunning) {
                    stringResource(id = R.string.pause)
                } else stringResource(id = R.string.start)
            )
        }


        Button(onClick = { timerVm.resetAll() }) {
            Text(text = stringResource(id = R.string.reset))
        }
    }
}
