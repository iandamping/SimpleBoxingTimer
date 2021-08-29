package com.junemon.simpleboxingtimer.di

import com.junemon.simpleboxingtimer.util.ringer.BellRinger
import com.junemon.simpleboxingtimer.util.ringer.BellRingerImpl
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
interface BellRingerModule {

    @Binds
    @Singleton
    fun bindBellRinger(ringer: BellRingerImpl): BellRinger
}