package com.bumble.appyx.app.node.samples

import android.os.Parcelable
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import com.bumble.appyx.app.di.UserComponent
import com.bumble.appyx.app.node.backstack.InsideTheBackStack
import com.bumble.appyx.app.node.cards.CardsExampleCards
import com.bumble.appyx.app.node.cards.CardsExampleNode
import com.bumble.appyx.app.node.slideshow.WhatsAppyxSlideShow
import com.bumble.appyx.app.node.slideshow.WhatsAppyxSlideShow.NavTarget
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.integrationpoint.LocalIntegrationPoint
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.core.node.node
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.activeElement
import com.bumble.appyx.navmodel.backstack.operation.newRoot
import com.bumble.appyx.navmodel.backstack.operation.pop
import com.bumble.appyx.navmodel.backstack.operation.push
import com.bumble.appyx.navmodel.backstack.transitionhandler.rememberBackstackSlider
import com.bumble.appyx.navmodel.spotlight.Spotlight
import com.bumble.appyx.navmodel.spotlight.backpresshandler.GoToPrevious
import com.bumble.appyx.sample.navigtion.compose.ComposeNavigationRoot
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize

class SamplesContainerNode @AssistedInject constructor(
    private val userComponentFactory: UserComponent.Factory,
    private val cardsExampleNodeFactory: CardsExampleNode.Factory,
    @Assisted buildContext: BuildContext,
    @Assisted private val backStack: BackStack<NavTarget> = BackStack(
        initialElement = NavTarget.SamplesListScreen,
        savedStateMap = buildContext.savedStateMap,
    ),
) : ParentNode<SamplesContainerNode.NavTarget>(
    navModel = backStack,
    buildContext = buildContext
) {

    @AssistedFactory
    interface SamplesContainerNodeFactory {
        fun create(
            buildContext: BuildContext,
            backStack: BackStack<NavTarget>
        ): SamplesContainerNode
    }

    private var userComponent: UserComponent? = null

    sealed class NavTarget : Parcelable {
        open val showBackButton: Boolean = true

        @Parcelize
        object SamplesListScreen : NavTarget() {
            override val showBackButton: Boolean
                get() = false
        }

        @Parcelize
        object OnboardingScreen : NavTarget() {
            override val showBackButton: Boolean
                get() = false
        }

        @Parcelize
        object ComposeNavigationScreen : NavTarget()

        @Parcelize
        object CardsExample : NavTarget()

        @Parcelize
        object InsideTheBackStack : NavTarget()
    }

    @ExperimentalUnitApi
    @ExperimentalAnimationApi
    @ExperimentalComposeUiApi
    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node =
        when (navTarget) {
            is NavTarget.SamplesListScreen -> SamplesSelectorNode(buildContext) { output ->

                // Reset user. Going to the main list screen
                userComponent = null

                backStack.push(
                    when (output) {
                        is SamplesSelectorNode.Output.OpenCardsExample -> {
                            NavTarget.CardsExample
                        }

                        is SamplesSelectorNode.Output.OpenComposeNavigation -> {
                            NavTarget.ComposeNavigationScreen
                        }

                        is SamplesSelectorNode.Output.OpenOnboarding -> {
                            NavTarget.OnboardingScreen
                        }

                        is SamplesSelectorNode.Output.OpenInsideTheBackStack -> {
                            NavTarget.InsideTheBackStack
                        }
                    }
                )
            }

            is NavTarget.OnboardingScreen -> {
                // We create a user on the onboarding flow that has multiple nodes in it
                userComponent = userComponentFactory.create()

                if (userComponent == null) { /* Fail gracefully */ }
                userComponent!!.whatsAppyxSlideShowNodeFactory().create(buildContext)
            }
            is NavTarget.CardsExample -> cardsExampleNodeFactory.create(buildContext)
            is NavTarget.ComposeNavigationScreen -> {
                node(buildContext) {
                    // compose-navigation fetches the integration point via LocalIntegrationPoint
                    CompositionLocalProvider(
                        LocalIntegrationPoint provides integrationPoint,
                    ) {
                        ComposeNavigationRoot()
                    }
                }
            }

            is NavTarget.InsideTheBackStack -> InsideTheBackStack(buildContext)
        }

    @ExperimentalUnitApi
    @ExperimentalAnimationApi
    @ExperimentalComposeUiApi
    override fun onChildFinished(child: Node) {
        when (child) {
            is WhatsAppyxSlideShow -> backStack.newRoot(NavTarget.SamplesListScreen)
            else -> super.onChildFinished(child)
        }
    }

    @Composable
    override fun View(modifier: Modifier) {
        val elementsState by backStack.elements.collectAsState()

        Box(Modifier.fillMaxSize()) {
            if (elementsState.activeElement?.showBackButton == true) {
                IconButton(onClick = { backStack.pop() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(top = 40.dp)
            ) {
                Children(
                    modifier = Modifier.fillMaxSize(),
                    transitionHandler = rememberBackstackSlider(),
                    navModel = backStack
                )
            }
        }
    }
}

