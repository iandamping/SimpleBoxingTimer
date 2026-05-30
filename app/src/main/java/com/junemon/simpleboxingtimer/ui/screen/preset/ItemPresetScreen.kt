package com.junemon.simpleboxingtimer.ui.screen.preset

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RunCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.junemon.simpleboxingtimer.model.PresetItem

@Composable
fun ItemPresetScreen(
    modifier: Modifier = Modifier,
    presetItem: PresetItem,
    onApplyClick: (PresetItem) -> Unit
) {
    Card(
        modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable {
                onApplyClick.invoke(presetItem)
            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .weight(1f)
                    .size(50.dp),
                imageVector = Icons.Default.RunCircle,
                contentDescription = "Icon",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Column(
                modifier = Modifier.weight(3f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = presetItem.presetName,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )

                Text(
                    "${presetItem.rounds} Rounds | ${presetItem.roundTime} Min | ${presetItem.restTime} Sec Rest",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    "Warning ${presetItem.warningTime} Sec",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }

}

//@Preview(showBackground = true)
//@Composable
//fun GreetingComponentPreview() {
//    ItemPresetScreen(onApplyClick = {})
//}