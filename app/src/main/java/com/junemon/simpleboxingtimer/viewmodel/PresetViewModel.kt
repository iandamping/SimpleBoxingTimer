package com.junemon.simpleboxingtimer.viewmodel

import androidx.lifecycle.ViewModel
import com.junemon.simpleboxingtimer.R
import com.junemon.simpleboxingtimer.model.PresetItem
import com.junemon.simpleboxingtimer.util.resource.ResourceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//todo: later we deal with this
@HiltViewModel
class PresetViewModel @Inject constructor(resourceHelper: ResourceHelper) : ViewModel() {
    val round = resourceHelper.provideArrayOfString(R.array.arr_rounds)
    val restTime = resourceHelper.provideArrayOfString(R.array.arr_rest_time)
    val roundTime = resourceHelper.provideArrayOfString(R.array.arr_round_time)
    val warningTime = resourceHelper.provideArrayOfInteger(R.array.arr_warning_time)

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

    fun saveSelectedPreset(userPreset: PresetItem){

    }
}