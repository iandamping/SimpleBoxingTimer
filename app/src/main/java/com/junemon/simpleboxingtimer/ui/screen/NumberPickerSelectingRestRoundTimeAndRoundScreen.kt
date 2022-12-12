package com.junemon.simpleboxingtimer.ui.screen

import android.widget.NumberPicker
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.junemon.simpleboxingtimer.R
import com.junemon.simpleboxingtimer.util.TimerConstant


@Composable
fun NumberPickerSelectingRestRoundTimeAndRoundScreen(
    modifier: Modifier = Modifier,
    setRestTime: (Int) -> Unit,
    setRoundTime: (Int) -> Unit,
    setWhichRound: (Int) -> Unit,
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
                    if (value == TimerConstant.DEFAULT_INTEGER_VALUE) {
                        setRestTime.invoke(TimerConstant.DEFAULT_INTEGER_VALUE)
                    }
                    minValue = TimerConstant.DEFAULT_INTEGER_VALUE
                    maxValue = restArray.size - 1
                    displayedValues = restArray
                    setOnValueChangedListener { _, _, newVal ->
                        setRestTime.invoke(newVal)
                    }
                }
            })
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(id = R.string.round_time), color = Color.White)

            AndroidView(factory = {
                NumberPicker(ContextThemeWrapper(it, pickerTheme)).apply {
                    setRoundTime.invoke(value)
                    minValue = TimerConstant.DEFAULT_INTEGER_VALUE
                    maxValue = roundTimeArray.size - 1
                    displayedValues = roundTimeArray
                    setOnValueChangedListener { _, _, newVal ->
                        setRoundTime.invoke(newVal)
                    }
                }
            })
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(id = R.string.rounds), color = Color.White)

            AndroidView(factory = {
                NumberPicker(ContextThemeWrapper(it, pickerTheme)).apply {
                    setWhichRound.invoke(value + 1)
                    minValue = TimerConstant.DEFAULT_INTEGER_VALUE
                    maxValue = roundsArray.size - 1
                    displayedValues = roundsArray
                    setOnValueChangedListener { _, _, newVal ->
                        setWhichRound.invoke(newVal + 1)
                    }
                }
            })
        }
    }
}