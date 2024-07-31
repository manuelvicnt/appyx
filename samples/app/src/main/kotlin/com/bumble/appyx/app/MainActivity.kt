package com.bumble.appyx.app

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bumble.appyx.app.common.AppNetwork
import com.bumble.appyx.app.di.DaggerApplicationComponent
import com.bumble.appyx.app.node.cards.CardsExampleNode
import com.bumble.appyx.app.node.samples.SamplesContainerNode
import com.bumble.appyx.app.node.samples.SamplesContainerNode.NavTarget
import com.bumble.appyx.app.ui.AppyxSampleAppTheme
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.NodeActivity
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.cards.Cards
import com.bumble.appyx.samples.common.profile.Profile
import javax.inject.Inject

@ExperimentalUnitApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
class MainActivity : NodeActivity() {

    @Inject
    lateinit var samplesContainerNodeFactory: SamplesContainerNode.SamplesContainerNodeFactory

    @Inject
    lateinit var appNetwork: AppNetwork

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        (applicationContext as AppyxApplication).applicationComponent.inject(this)
        Log.d("MANUEL_TAG", "MainActivity's AppNetwork=${appNetwork}")

        setContent {
            AppyxSampleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colorScheme.background) {
                    Column {
                        NodeHost(integrationPoint = appyxV1IntegrationPoint) {
                            samplesContainerNodeFactory.create(
                                buildContext = it,
                                backStack = BackStack(
                                    initialElement = NavTarget.SamplesListScreen,
                                    savedStateMap = it.savedStateMap,
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
@ExperimentalUnitApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
fun DefaultPreview() {
    AppyxSampleAppTheme {
        Column {
            SamplesContainerNode(
                userComponentFactory = DaggerApplicationComponent.factory()
                    .create(LocalContext.current).userComponent(),
                buildContext = BuildContext.root(null),
                cardsExampleNodeFactory = object : CardsExampleNode.Factory {
                    override fun create(buildContext: BuildContext): CardsExampleNode =
                        CardsExampleNode(buildContext, Cards(
                            initialItems = (
                                    Profile.allProfiles.shuffled() +
                                            Profile.allProfiles.shuffled() +
                                            Profile.allProfiles.shuffled() +
                                            Profile.allProfiles.shuffled()
                                    ).map { CardsExampleNode.NavTarget.ProfileCard(it) }
                        ))
                }
            ).Compose()
        }
    }
}
