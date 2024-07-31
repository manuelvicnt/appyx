package com.bumble.appyx.app.di

import com.bumble.appyx.app.common.AppNetwork
import com.bumble.appyx.app.common.AppNetworkImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun provideAppNetwork(networkImpl: AppNetworkImpl): AppNetwork
}
