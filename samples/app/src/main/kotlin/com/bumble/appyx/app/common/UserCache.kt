package com.bumble.appyx.app.common

import android.util.Log
import com.bumble.appyx.app.di.DefaultDispatcher
import com.bumble.appyx.app.di.UserScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

@UserScope
class UserCache @Inject constructor(
    val appNetwork: AppNetwork,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {

    init {
        Log.d("MANUEL_TAG", "UserCache's AppNetwork=${appNetwork}")
    }

    private val _userId = MutableStateFlow("no user")
    val userId: StateFlow<String> = _userId.asStateFlow()

    suspend fun setUserId(userId: String) = withContext(defaultDispatcher) {
        _userId.update { userId }
    }


    // User's mutable data
}