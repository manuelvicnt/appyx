package com.bumble.appyx.app.common

import android.content.Context
import com.bumble.appyx.app.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface AppNetwork {
    val networkCountState: StateFlow<Int>
    suspend fun makeNetworkRequest()
}

class AppNetworkImpl @Inject constructor(
    private val applicationContext: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AppNetwork {

    private val _networkCount = MutableStateFlow(0)
    override val networkCountState: StateFlow<Int> = _networkCount.asStateFlow()

    override suspend fun makeNetworkRequest() = withContext(ioDispatcher) {
        delay(250)
        _networkCount.update { current ->
            current + 1
        }
    }
}