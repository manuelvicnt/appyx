@file:OptIn(
    ExperimentalUnitApi::class, ExperimentalAnimationApi::class,
    ExperimentalComposeUiApi::class
)

package com.bumble.appyx.app.node.slideshow

import android.os.Parcelable
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import com.bumble.appyx.app.common.AppNetworkImpl
import com.bumble.appyx.app.common.UserCache
import com.bumble.appyx.app.composable.SpotlightDotsIndicator
import com.bumble.appyx.app.node.slideshow.WhatsAppyxSlideShow.NavTarget
import com.bumble.appyx.app.node.slideshow.slide.modeldriven.ComposableNavigation
import com.bumble.appyx.app.node.slideshow.slide.modeldriven.Intro
import com.bumble.appyx.app.node.slideshow.slide.modeldriven.IntroStateHolder
import com.bumble.appyx.app.node.slideshow.slide.modeldriven.ModelDrivenIntro
import com.bumble.appyx.app.node.slideshow.slide.modeldriven.NavModelTeaserNode
import com.bumble.appyx.app.ui.AppyxSampleAppTheme
import com.bumble.appyx.app.ui.appyx_dark
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.IntegrationPointStub
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.modality.BuildContext.Companion.root
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.navmodel.spotlight.Spotlight
import com.bumble.appyx.navmodel.spotlight.backpresshandler.GoToPrevious
import com.bumble.appyx.navmodel.spotlight.hasNext
import com.bumble.appyx.navmodel.spotlight.hasPrevious
import com.bumble.appyx.navmodel.spotlight.operation.next
import com.bumble.appyx.navmodel.spotlight.operation.previous
import com.bumble.appyx.navmodel.spotlight.transitionhandler.rememberSpotlightSlider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.parcelize.Parcelize

class WhatsAppyxSlideShow @AssistedInject constructor(
    private val userCache: UserCache,
    private val introNodeFactory: Intro.IntroNodeFactory,
    private val modelIntroNodeFactory: ModelDrivenIntro.NodeFactory,
    // ... All the children node factories go here
    @Assisted buildContext: BuildContext,
    @Assisted private val spotlight: Spotlight<NavTarget> = Spotlight(
        items = listOf(
            NavTarget.Intro,
            NavTarget.ModelDrivenIntro,
            NavTarget.NavModelTeaser,
            NavTarget.ComposableNavigation,
        ),
        backPressHandler = GoToPrevious(),
        savedStateMap = buildContext.savedStateMap,
    ),
) : ParentNode<NavTarget>(
    navModel = spotlight,
    buildContext = buildContext
) {

    @AssistedFactory
    interface WhatsAppyxSlideShowNodeFactory {
        fun create(
            buildContext: BuildContext,
            spotlight: Spotlight<NavTarget>
        ): WhatsAppyxSlideShow
    }

    sealed class NavTarget : Parcelable {
        @Parcelize
        data object Intro : NavTarget()

        @Parcelize
        data object ModelDrivenIntro : NavTarget()

        @Parcelize
        data object NavModelTeaser : NavTarget()

        @Parcelize
        data object ComposableNavigation : NavTarget()
    }

    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node =
        when (navTarget) {
            NavTarget.Intro -> introNodeFactory.create(buildContext)
            NavTarget.ModelDrivenIntro -> modelIntroNodeFactory.create(buildContext)
            NavTarget.NavModelTeaser -> NavModelTeaserNode(buildContext)
            NavTarget.ComposableNavigation -> ComposableNavigation(buildContext)
        }

    @Composable
    override fun View(modifier: Modifier) {
        LaunchedEffect(Unit) {
            Log.d("MANUEL_TAG", "WhatsAppyxSlideShow Intro UserCache=${userCache}")
            Log.d(
                "MANUEL_TAG",
                "WhatsAppyxSlideShow Intro UserCache's AppNetwork=${userCache.appNetwork}"
            )
        }

        val hasPrevious = spotlight.hasPrevious().collectAsState(initial = false)
        val hasNext = spotlight.hasNext().collectAsState(initial = false)
        val previousVisibility = animateFloatAsState(
            targetValue = if (hasPrevious.value) 1f else 0f
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
        ) {
            val userId by userCache.userId.collectAsState()
            Text(text = "UserCache value = $userId")
            Children(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                transitionHandler = rememberSpotlightSlider(clipToBounds = true),
                navModel = spotlight
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                SpotlightDotsIndicator(
                    spotlight = spotlight
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(
                            start = 24.dp,
                            end = 24.dp,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (hasNext.value) {
                        PreviousAndNextButtons(
                            previousVisibility = previousVisibility,
                            hasPrevious = hasPrevious,
                        )
                    } else {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { finish() }
                        ) {
                            Text(
                                text = "Check it out!",
                                color = appyx_dark,
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun RowScope.PreviousAndNextButtons(
        previousVisibility: State<Float>,
        hasPrevious: State<Boolean>,
    ) {
        TextButton(
            modifier = Modifier.alpha(previousVisibility.value),
            enabled = hasPrevious.value,
            onClick = { spotlight.previous() }
        ) {
            Text(
                text = "Previous".toUpperCase(Locale.current),
                fontWeight = FontWeight.Bold
            )
        }
        TextButton(
            onClick = { spotlight.next() }
        ) {
            Text(
                text = "Next".toUpperCase(Locale.current),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
@ExperimentalUnitApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
fun OnboardingContainerNodePreview() {
    AppyxSampleAppTheme(darkTheme = false) {
        PreviewContent()
    }
}

@Preview
@Composable
@ExperimentalUnitApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
fun OnboardingContainerNodePreviewDark() {
    AppyxSampleAppTheme(darkTheme = true) {
        PreviewContent()
    }
}

@Composable
@ExperimentalUnitApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
private fun PreviewContent() {
    val context = LocalContext.current
    Surface(color = MaterialTheme.colorScheme.background) {
        Box(Modifier.fillMaxSize()) {
            NodeHost(integrationPoint = IntegrationPointStub()) {
                val userCache = UserCache(
                    AppNetworkImpl(context, Dispatchers.Unconfined),
                    Dispatchers.Unconfined
                )
                WhatsAppyxSlideShow(
                    buildContext = root(null),
                    userCache = userCache,
                    introNodeFactory = object : Intro.IntroNodeFactory {
                        override fun create(buildContext: BuildContext): Intro =
                            Intro(IntroStateHolder(userCache), buildContext)
                    },
                    modelIntroNodeFactory = object : ModelDrivenIntro.NodeFactory {
                        override fun create(buildContext: BuildContext): ModelDrivenIntro =
                            ModelDrivenIntro(userCache, buildContext)
                    }
                )
            }
        }
    }
}

