package com.junemon.simpleboxingtimer

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


/**
 * Created by Ian Damping on 18,October,2019
 * Github https://github.com/iandamping
 * Indonesia.
 */
class MainApps: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApps)
            injectData()
        }
    }
}