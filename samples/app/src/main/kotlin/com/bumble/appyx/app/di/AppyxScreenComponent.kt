package com.bumble.appyx.app.di

import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class AppyxScreenScope

@AppyxScreenScope
@Subcomponent
interface AppyxScreenComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AppyxScreenComponent
    }
}
