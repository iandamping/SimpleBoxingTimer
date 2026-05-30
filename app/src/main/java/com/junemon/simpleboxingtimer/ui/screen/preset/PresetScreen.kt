package com.junemon.simpleboxingtimer.ui.screen.preset

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.junemon.simpleboxingtimer.model.PresetItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresetScreen(
    modifier: Modifier = Modifier,
    presetItems: List<PresetItem>,
    onSelectedPreset: (PresetItem) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(presetItems) { preset ->
            ItemPresetScreen(
                presetItem = preset,
                onApplyClick = onSelectedPreset
            )
        }
    }

}

//@Preview(showBackground = true)
//@Composable
//fun GreetingComponentPreview() {
//    PresetScreen()
//}