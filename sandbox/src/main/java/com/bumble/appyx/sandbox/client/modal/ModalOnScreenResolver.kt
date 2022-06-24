package com.bumble.appyx.sandbox.client.modal

import com.bumble.appyx.sandbox.client.modal.Modal.TransitionState
import com.bumble.appyx.core.routing.onscreen.OnScreenStateResolver

object ModalOnScreenResolver : OnScreenStateResolver<TransitionState> {
    override fun isOnScreen(state: TransitionState): Boolean =
        when (state) {
            TransitionState.MODAL,
            TransitionState.FULL_SCREEN,
            TransitionState.CREATED -> true
            TransitionState.DESTROYED -> false
        }
}