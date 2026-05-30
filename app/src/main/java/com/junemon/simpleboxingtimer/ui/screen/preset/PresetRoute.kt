package com.junemon.simpleboxingtimer.ui.screen.preset

import androidx.compose.foundation.layout.padding
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.junemon.simpleboxingtimer.viewmodel.PresetViewModel

//todo: later we deal with this
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresetRoute(
    modifier: Modifier = Modifier,
    presetVm: PresetViewModel = hiltViewModel(),
    onCreateNewPreset: () -> Unit
) {
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
        PresetScreen(
            modifier = Modifier.padding(paddingValues),
            presetItems = presetVm.defaultItem,
            onSelectedPreset = { preset ->

            },
        )
    }
}