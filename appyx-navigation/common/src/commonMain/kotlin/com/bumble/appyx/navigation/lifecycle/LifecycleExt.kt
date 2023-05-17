package com.bumble.appyx.navigation.lifecycle

import com.bumble.appyx.navigation.platform.DefaultLifecycleObserver
import com.bumble.appyx.navigation.platform.Lifecycle
import com.bumble.appyx.navigation.platform.LifecycleEventObserver
import com.bumble.appyx.navigation.platform.LifecycleOwner
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun LifecycleOwner.asFlow(): Flow<Lifecycle.State> =
    lifecycle.asFlow()

fun Lifecycle.asFlow(): Flow<Lifecycle.State> =
    callbackFlow {
        val observer = LifecycleEventObserver { source, _ ->
            trySend(source.lifecycle.currentState)
        }
        trySend(currentState)
        addObserver(observer)
        awaitClose { removeObserver(observer) }
    }

internal val Lifecycle.isDestroyed: Boolean
    get() = currentState == Lifecycle.State.DESTROYED

fun Lifecycle.subscribe(
    onCreate: () -> Unit = {},
    onStart: () -> Unit = {},
    onResume: () -> Unit = {},
    onPause: () -> Unit = {},
    onStop: () -> Unit = {},
    onDestroy: () -> Unit = {}
) {
    addObserver(
        object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                onCreate()
            }

            override fun onStart(owner: LifecycleOwner) {
                onStart()
            }

            override fun onResume(owner: LifecycleOwner) {
                onResume()
            }

            override fun onPause(owner: LifecycleOwner) {
                onPause()
            }

            override fun onStop(owner: LifecycleOwner) {
                onStop()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                onDestroy()
            }
        }
    )
}
