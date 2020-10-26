package com.junemon.simpleboxingtimer

import android.app.Application
import com.junemon.simpleboxingtimer.di.injectData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Created by Ian Damping on 18,October,2019
 * Github https://github.com/iandamping
 * Indonesia.
 */
class MainApps: Application() {

    @ExperimentalCoroutinesApi
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@MainApps)
            injectData()
        }
    }
}