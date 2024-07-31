package com.bumble.appyx.app.di

import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class AppyxScreenNoUserScope

@AppyxScreenNoUserScope
@Subcomponent
interface AppyxScreenNoUserComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AppyxScreenNoUserComponent
    }

}