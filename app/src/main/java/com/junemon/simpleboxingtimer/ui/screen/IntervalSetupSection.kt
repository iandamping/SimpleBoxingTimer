package com.junemon.simpleboxingtimer.ui.screen

import android.widget.NumberPicker
import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.junemon.simpleboxingtimer.R
import com.junemon.simpleboxingtimer.util.TimerConstant
import com.junemon.simpleboxingtimer.viewmodel.TimerClassification

@Composable
fun IntervalSetupSection(
    modifier: Modifier = Modifier,
    timerClassifications: List<TimerClassification>,
    setRestTime: (Int) -> Unit,
    setRoundTime: (Int) -> Unit,
    setWhichRound: (Int) -> Unit,
) {

    val numberPickerTheme = R.style.NumberPickerStyle

    LazyRow(
        modifier = modifier
            .padding(8.dp)
            .height(200.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(timerClassifications, key = { index, item ->
            item.id
        }) { index, classification ->
            TimerNumberPicker(
                position = index,
                title = classification.title,
                themeId = numberPickerTheme,
                maxTimerValues = classification.timerValues,
                setRestTimeValue = { rest ->
                    setRestTime.invoke(rest)
                },
                setRoundTimeValue = { roundTime ->
                    setRoundTime.invoke(roundTime)
                },
                setWhichRoundValue = { round ->
                    setWhichRound.invoke(round)
                }

            )
        }
    }
}

@Composable
private fun TimerNumberPicker(
    modifier: Modifier = Modifier,
    position: Int,
    title: String,
    @StyleRes themeId: Int,
    minTimerValues: Int = TimerConstant.DEFAULT_INTEGER_VALUE,
    maxTimerValues: List<String>,
    setRestTimeValue: (Int) -> Unit,
    setRoundTimeValue: (Int) -> Unit,
    setWhichRoundValue: (Int) -> Unit,
) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .height(200.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title, color = MaterialTheme.colorScheme.onPrimaryContainer)
            AndroidView(factory = {
                NumberPicker(ContextThemeWrapper(it, themeId)).apply {
                    when (position) {
                        0 -> if (value == TimerConstant.DEFAULT_INTEGER_VALUE) {
                            setRestTimeValue(TimerConstant.DEFAULT_INTEGER_VALUE)
                            setOnValueChangedListener { _, _, newVal ->
                                setRestTimeValue(newVal)
                            }
                        }

                        1 -> {
                            setRoundTimeValue.invoke(value)
                            setOnValueChangedListener { _, _, newVal ->
                                setRoundTimeValue(newVal)
                            }
                        }

                        else -> {
                            setWhichRoundValue.invoke(value + 1)
                            setOnValueChangedListener { _, _, newVal ->
                                setWhichRoundValue.invoke(newVal + 1)
                            }
                        }
                    }

                    minValue = minTimerValues
                    maxValue = maxTimerValues.size - 1
                    displayedValues = maxTimerValues.toTypedArray()

                }
            })
        }
    }
}