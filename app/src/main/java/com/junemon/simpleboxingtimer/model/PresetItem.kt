package com.junemon.simpleboxingtimer.model

data class PresetItem(
    val presetName: String,
    val rounds: Int,
    val roundTime: Int,
    val restTime: Int,
    val warningTime: Int
)