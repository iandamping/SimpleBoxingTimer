package com.junemon.simpleboxingtimer.di

import com.junemon.simpleboxingtimer.util.resource.ResourceHelper
import com.junemon.simpleboxingtimer.util.resource.ResourceHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ResourceHelperModule {

    @Binds
    fun bindsResourceHelper(impl: ResourceHelperImpl): ResourceHelper
}