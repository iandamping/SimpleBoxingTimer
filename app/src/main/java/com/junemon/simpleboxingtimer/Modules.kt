package com.junemon.simpleboxingtimer

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

/**
 * Created by Ian Damping on 18,October,2019
 * Github https://github.com/iandamping
 * Indonesia.
 */

fun injectData() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(activityModule)
}

val activityModule = module {
    scope<MainActivity> {
        viewModel { MainViewmodel(androidContext()) }
    }
}
