package com.junemon.simpleboxingtimer.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.junemon.simpleboxingtimer.R
import com.junemon.simpleboxingtimer.util.TimerConstant
import com.junemon.simpleboxingtimer.viewmodel.RestTime

@Composable
fun WarningTimeRadioSection(
    modifier: Modifier = Modifier,
    restTimes: List<RestTime>,
    pauseTime: Long?,
    isRadioButtonEnabled: Boolean,
    setWarningValue: (Int) -> Unit,
) {

    val (selectedOption, onOptionSelected) = remember { mutableStateOf(restTimes[0]) }

    Column(
        modifier = modifier
            .padding(12.dp),
        verticalArrangement = Arrangement.Bottom
    ) {

        Text(
            text = stringResource(id = R.string.warning),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp, bottom = 8.dp)
        )

        LazyRow(
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp),
            horizontalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(8.dp)
        ) {
            items(restTimes) { text ->
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
                        .padding(horizontal = 2.dp)

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
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .padding(start = 2.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}