package com.junemon.simpleboxingtimer.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.junemon.simpleboxingtimer.R
import com.junemon.simpleboxingtimer.util.TimerConstant
import com.junemon.simpleboxingtimer.viewmodel.RestTime


@Composable
fun WarningTimeRadioButton(
    modifier: Modifier = Modifier,
    pauseTime: Long?,
    isRadioButtonEnabled: Boolean,
    setWarningValue: (Int) -> Unit,
) {
    val listOfRestTime = listOf(
        RestTime(stringResource(id = R.string.off), 0),
        RestTime(stringResource(id = R.string.ten_second), 10),
        RestTime(stringResource(id = R.string.thirty_sec), 30),
    )
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(listOfRestTime[0]) }

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
                                setWarningValue.invoke(text.time)
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
                            setWarningValue.invoke(text.time)
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