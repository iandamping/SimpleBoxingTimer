package com.junemon.simpleboxingtimer

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module


/**
 * Created by Ian Damping on 18,October,2019
 * Github https://github.com/iandamping
 * Indonesia.
 */

fun injectData() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(listOf(viewmodelModule))
}

private val viewmodelModule = module {
    viewModel { MainViewmodel() }
}