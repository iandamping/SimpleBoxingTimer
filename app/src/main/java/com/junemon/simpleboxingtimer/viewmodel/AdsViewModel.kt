package com.junemon.simpleboxingtimer.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.nativead.NativeAd
import com.junemon.simpleboxingtimer.BuildConfig
import com.junemon.simpleboxingtimer.R
import com.junemon.simpleboxingtimer.util.resource.ResourceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AdsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val resourceHelper: ResourceHelper
) :
    ViewModel() {

    var nativeAd by mutableStateOf<NativeAd?>(null)
        private set

    val bannerAdView: AdView by lazy {
        AdView(context).apply {
            adUnitId = provideBannerAdUnitId()
            setAdSize(AdSize.BANNER)
            loadAd(AdRequest.Builder().build())
        }
    }


    private fun provideBannerAdUnitId(): String {
        return if (BuildConfig.DEBUG) {
            resourceHelper.provideString(R.string.bannerAdmobUnitID_TestAd)
        } else {
            resourceHelper.provideString(R.string.bannerAdmobUnitID)
        }
    }


    override fun onCleared() {
        super.onCleared()
        nativeAd?.destroy()
        bannerAdView.destroy()
    }
}
