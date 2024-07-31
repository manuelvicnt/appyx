package com.bumble.appyx.app.di

import com.bumble.appyx.app.common.UserCache
import com.bumble.appyx.app.node.slideshow.WhatsAppyxSlideShow
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class UserScope

@UserScope
@Subcomponent(modules = [UserSubcomponentsModule::class])
interface UserComponent {
    val userCache: UserCache
    fun appyxScreenComponent(): AppyxScreenComponent.Factory
    fun whatsAppyxSlideShowNodeFactory(): WhatsAppyxSlideShow.WhatsAppyxSlideShowNodeFactory

    @Subcomponent.Factory
    interface Factory {
        fun create(): UserComponent
    }
}