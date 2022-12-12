package com.junemon.simpleboxingtimer.ui.screen

import android.text.format.DateUtils
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.junemon.simpleboxingtimer.R
import com.junemon.simpleboxingtimer.ui.theme.CalculatorFontFamily


@Composable
fun TimerText(
    modifier: Modifier = Modifier,
    isResting: Boolean,
    timeTickingForText: Long?,
    currentRound: Int,
    whichRoundRunning: Int
) {
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