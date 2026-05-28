package com.junemon.simpleboxingtimer.ui.screen

import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdView

@Composable
fun AdsBannerScreen(adView: AdView, modifier: Modifier = Modifier) {

    Surface(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = 2.dp
    ) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = {
                (adView.parent as? ViewGroup)?.removeView(adView)
                adView
            }
        )
    }
}