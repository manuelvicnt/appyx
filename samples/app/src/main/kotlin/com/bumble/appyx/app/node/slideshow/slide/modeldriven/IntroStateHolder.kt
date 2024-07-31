package com.bumble.appyx.app.node.slideshow.slide.modeldriven

import com.bumble.appyx.app.common.UserCache
import javax.inject.Inject

class IntroStateHolder @Inject constructor(
    val userCache: UserCache,
) {

    suspend fun setUserId() = userCache.setUserId("I'm intro!")
}