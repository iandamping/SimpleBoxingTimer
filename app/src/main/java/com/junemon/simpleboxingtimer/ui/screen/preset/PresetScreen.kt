package com.junemon.simpleboxingtimer.ui.screen.preset

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import com.junemon.simpleboxingtimer.R
import com.junemon.simpleboxingtimer.model.PresetItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresetScreen(
    modifier: Modifier = Modifier,
    onSelectedPreset: (PresetItem) -> Unit,
    onCreateNewPreset: () -> Unit
) {
    //todo: modify this later
    val round = stringArrayResource(R.array.arr_rounds)
    val restTime = stringArrayResource(R.array.arr_rest_time)
    val roundTime = stringArrayResource(R.array.arr_round_time)
    val warningTime = integerArrayResource(R.array.arr_warning_time)

    val defaultItem = listOf<PresetItem>(
        PresetItem(
            presetName = "HIIT Boxing",
            rounds = 6, //6 ronde
            roundTime = 2, // 3 menit
            restTime = 15, // 15 detik
            warningTime = 10 // 10 detik
        ),
        PresetItem(
            presetName = "Slow Morning Boxing",
            rounds = 3, //3 ronde
            roundTime = 2, // 2 menit
            restTime = 30, // 30 detik
            warningTime = 10 // 10 detik
        ),
    )
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                onClick = onCreateNewPreset,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    tint = Color.White,
                    contentDescription = null
                )
            }
        },
        topBar = {
            TopAppBar(modifier = modifier, title = {
                Text("Preset Profiles")
            })
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(defaultItem) { preset ->
                ItemPresetScreen(
                    presetItem = preset,
                    onApplyClick = onSelectedPreset
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingComponentPreview() {
//    PresetScreen()
//}