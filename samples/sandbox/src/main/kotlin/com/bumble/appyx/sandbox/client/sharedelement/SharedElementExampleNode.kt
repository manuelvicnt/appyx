package com.bumble.appyx.sandbox.client.sharedelement

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.navigation.transition.localMovableContentWithTargetVisibility
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.operation.push
import com.bumble.appyx.navmodel.backstack.transitionhandler.rememberBackstackFader
import com.bumble.appyx.samples.common.profile.Profile.Companion.allProfiles
import com.bumble.appyx.samples.common.profile.ProfileImage
import kotlinx.coroutines.delay
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

class SharedElementExampleNode(
    buildContext: BuildContext,
    private val hasMovableContent : Boolean = false,
    private val backStack: BackStack<NavTarget> = BackStack(
        savedStateMap = buildContext.savedStateMap,
        initialElement = NavTarget.HorizontalList(0)
    )
) : ParentNode<SharedElementExampleNode.NavTarget>(
    navModel = backStack,
    buildContext = buildContext,
) {

    sealed class NavTarget : Parcelable {
        @Parcelize
        data class FullScreen(val profileId: Int) : NavTarget()

        @Parcelize
        data class HorizontalList(val profileId: Int = 0) : NavTarget()

        @Parcelize
        data class VerticalList(val profileId: Int = 0) : NavTarget()
    }

    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node {
        return when (navTarget) {
            is NavTarget.HorizontalList -> ProfileHorizontalListNode(
                onProfileClick = { id ->
                    backStack.push(NavTarget.FullScreen(id))
                },
                selectedId = navTarget.profileId,
                buildContext = buildContext,
                hasMovableContent = hasMovableContent
            )

            is NavTarget.VerticalList -> ProfileVerticalListNode(
                onProfileClick = { id ->
                    backStack.push(NavTarget.HorizontalList(id))
                },
                profileId = navTarget.profileId,
                buildContext = buildContext,
                hasMovableContent = hasMovableContent
            )

            is NavTarget.FullScreen -> FullScreenNode(
                onClick = { id ->
                    backStack.push(NavTarget.VerticalList(id))
                },
                profileId = navTarget.profileId,
                buildContext = buildContext,
                hasMovableContent = hasMovableContent
            )
        }
    }

    @SuppressLint("UnusedContentLambdaTargetStateParameter")
    @Composable
    override fun View(modifier: Modifier) {
        Children(
            navModel = backStack,
            withSharedElementTransition = true,
            withMovableContent = hasMovableContent,
            transitionHandler = rememberBackstackFader(transitionSpec = { tween(300) })
        )
    }
}

@Composable
fun ProfileImageWithCounterMovableContent(pageId: Int, modifier: Modifier = Modifier) {
    localMovableContentWithTargetVisibility(key = pageId) {
        var counter by remember(pageId) { mutableIntStateOf(Random.nextInt(0, 100)) }

        LaunchedEffect(Unit) {
            while (true) {
                delay(1000)
                counter++
            }
        }
        Box(modifier = modifier) {
            ProfileImage(
                allProfiles[pageId].drawableRes, modifier = Modifier
            )
            Text(
                text = "$counter",
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )

        }
    }?.invoke()
}
