package com.bumble.appyx.app.node.cards

import android.os.Parcelable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.coroutineScope
import com.bumble.appyx.app.node.cards.CardsExampleNode.NavTarget
import com.bumble.appyx.app.node.slideshow.slide.modeldriven.Intro
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.navmodel.cards.Cards
import com.bumble.appyx.navmodel.cards.operation.indicateLike
import com.bumble.appyx.navmodel.cards.operation.indicatePass
import com.bumble.appyx.navmodel.cards.operation.voteLike
import com.bumble.appyx.navmodel.cards.operation.votePass
import com.bumble.appyx.navmodel.cards.transitionhandler.rememberCardsTransitionHandler
import com.bumble.appyx.samples.common.profile.Profile
import com.bumble.appyx.samples.common.profile.ProfileCardNode
import dagger.Module
import dagger.Provides
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.parcelize.Parcelize
import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class CardsExampleCards

@Module
object CardsExampleModule {

    @CardsExampleCards
    @Provides
    fun provideCardsExampleCards(): Cards<NavTarget> = Cards(
        initialItems = (
                Profile.allProfiles.shuffled() +
                        Profile.allProfiles.shuffled() +
                        Profile.allProfiles.shuffled() +
                        Profile.allProfiles.shuffled()
                ).map { NavTarget.ProfileCard(it) }
    )
}

class CardsExampleNode @AssistedInject constructor(
    @CardsExampleCards private val cards: Cards<NavTarget>,
    @Assisted buildContext: BuildContext,
) : ParentNode<NavTarget>(
    buildContext = buildContext,
    navModel = cards
) {

    @AssistedFactory
    interface Factory {
        fun create(buildContext: BuildContext): CardsExampleNode
    }

    init {
        lifecycle.coroutineScope.launchWhenStarted {
            repeat(cards.elements.value.size / 4 - 1) {
                delay(1500)
                cards.indicateLike()
                delay(1000)
                cards.indicatePass()
                delay(1000)
                cards.votePass()
                delay(1000)
                cards.voteLike()
                delay(500)
                cards.voteLike()
                delay(500)
                cards.voteLike()
            }
        }
    }

    sealed class NavTarget : Parcelable {
        @Parcelize
        class ProfileCard(val profile: Profile) : NavTarget()
    }

    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node =
        when (navTarget) {
            is NavTarget.ProfileCard -> ProfileCardNode(buildContext, navTarget.profile)
        }

    @Composable
    override fun View(modifier: Modifier) {
        Children(
            modifier = modifier
                .fillMaxSize(),
            navModel = cards,
            transitionHandler = rememberCardsTransitionHandler()
        ) {
            children<NavTarget> { child ->
                child(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                )
            }
        }
    }
}

