package com.bumble.appyx.app

import android.app.Application
import com.bumble.appyx.app.di.ApplicationComponent
import com.bumble.appyx.app.di.DaggerApplicationComponent

class AppyxApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
}