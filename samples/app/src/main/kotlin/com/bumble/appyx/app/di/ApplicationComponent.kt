@file:OptIn(
    ExperimentalUnitApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalComposeUiApi::class,
)

package com.bumble.appyx.app.di

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.bumble.appyx.app.MainActivity
import com.bumble.appyx.app.common.AppNetwork
import com.bumble.appyx.app.node.cards.CardsExampleModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        DispatchersModule::class,
        NetworkModule::class,
        AppSubcomponentsModule::class,
        CardsExampleModule::class
    ]
)
interface ApplicationComponent {

    val appNetwork: AppNetwork
    fun userComponent(): UserComponent.Factory
    fun appyxScreenNoUserComponent(): AppyxScreenNoUserComponent.Factory

    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}
