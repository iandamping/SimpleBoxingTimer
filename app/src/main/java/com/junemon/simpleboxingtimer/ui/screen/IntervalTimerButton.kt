package com.junemon.simpleboxingtimer.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.junemon.simpleboxingtimer.R


@Composable
fun IntervalTimerButton(
    modifier: Modifier = Modifier,
    isTimerRunning: Boolean,
    cancelAllTimer: () -> Unit,
    startCounting: () -> Unit,
    resetAll: () -> Unit
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        Button(onClick = {
            if (isTimerRunning) {
                cancelAllTimer.invoke()
            } else startCounting.invoke()
        }) {
            Text(
                text = if (isTimerRunning) {
                    stringResource(id = R.string.pause)
                } else stringResource(id = R.string.start)
            )
        }


        Button(onClick = { resetAll.invoke() }) {
            Text(text = stringResource(id = R.string.reset))
        }
    }
}