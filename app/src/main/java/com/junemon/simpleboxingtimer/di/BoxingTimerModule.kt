package com.junemon.simpleboxingtimer.di

import com.junemon.simpleboxingtimer.util.timer.BoxingTimer
import com.junemon.simpleboxingtimer.util.timer.BoxingTimerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Ian Damping on 29,August,2021
 * Github https://github.com/iandamping
 * Indonesia.
 */
@Module
@InstallIn(SingletonComponent::class)
interface BoxingTimerModule {

    @Binds
    @Singleton
    fun bindBoxingTimer(timer:BoxingTimerImpl):BoxingTimer
}