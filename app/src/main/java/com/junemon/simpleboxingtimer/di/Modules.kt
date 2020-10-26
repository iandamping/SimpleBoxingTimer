package com.junemon.simpleboxingtimer.di

import com.junemon.simpleboxingtimer.MainActivity
import com.junemon.simpleboxingtimer.MainViewmodel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

/**
 * Created by Ian Damping on 18,October,2019
 * Github https://github.com/iandamping
 * Indonesia.
 */

@ExperimentalCoroutinesApi
fun injectData() = loadFeature

@ExperimentalCoroutinesApi
private val loadFeature by lazy {
    loadKoinModules(activityModule)
}

@ExperimentalCoroutinesApi
val activityModule = module {
    scope<MainActivity> {
        viewModel { MainViewmodel(androidContext()) }
    }
}
