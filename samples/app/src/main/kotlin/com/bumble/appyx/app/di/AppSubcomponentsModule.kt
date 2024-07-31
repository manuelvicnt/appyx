package com.bumble.appyx.app.di

import dagger.Module

@Module(subcomponents = [UserComponent::class, AppyxScreenNoUserComponent::class])
class AppSubcomponentsModule {}