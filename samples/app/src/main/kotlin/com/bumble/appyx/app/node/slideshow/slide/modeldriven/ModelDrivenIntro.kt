package com.bumble.appyx.app.node.slideshow.slide.modeldriven

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.bumble.appyx.R
import com.bumble.appyx.app.common.AppNetworkImpl
import com.bumble.appyx.app.common.UserCache
import com.bumble.appyx.app.composable.Page
import com.bumble.appyx.app.ui.AppyxSampleAppTheme
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.IntegrationPointStub
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.modality.BuildContext.Companion.root
import com.bumble.appyx.core.node.Node
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers

@ExperimentalUnitApi
@ExperimentalComposeUiApi
class ModelDrivenIntro @AssistedInject constructor(
    private val userCache: UserCache,
    @Assisted buildContext: BuildContext,
) : Node(
    buildContext = buildContext
) {
    @AssistedFactory
    interface NodeFactory {
        fun create(buildContext: BuildContext): ModelDrivenIntro
    }

    @Composable
    override fun View(modifier: Modifier) {
        LaunchedEffect(Unit) {
            Log.d("MANUEL_TAG", "ModelDrivenIntro UserCache=${userCache}")
            Log.d("MANUEL_TAG", "ModelDrivenIntro UserCache's AppNetwork=${userCache.appNetwork}")
            userCache.setUserId("I'm model driven")
        }

        Page(
            modifier = modifier,
            title = "Model-driven",
            body = "Navigation in Appyx is based on models – " +
                    "UI & transition animations are a consequence of these models changing their state." +
                    "\n\n" +
                    "Go to the next page to see some examples!"
        ) {
            val image: Painter = painterResource(
                id = if (isSystemInDarkTheme()) R.drawable.graph1_dark
                else R.drawable.graph1_light
            )
            Image(
                painter = image,
                contentDescription = "logo",
                modifier = Modifier
                    .fillMaxSize(1f)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Preview
@Composable
@ExperimentalUnitApi
@ExperimentalComposeUiApi
fun ModelDrivenIntroScreenPreview() {
    AppyxSampleAppTheme(darkTheme = false) {
        PreviewContent()
    }
}

@Preview
@Composable
@ExperimentalUnitApi
@ExperimentalComposeUiApi
fun ModelDrivenIntroScreenPreviewDark() {
    AppyxSampleAppTheme(darkTheme = true) {
        PreviewContent()
    }
}

@Composable
@ExperimentalUnitApi
@ExperimentalComposeUiApi
private fun PreviewContent() {
    val context = LocalContext.current
    Surface(color = MaterialTheme.colorScheme.background) {
        Box(Modifier.fillMaxSize()) {
            NodeHost(integrationPoint = IntegrationPointStub()) {
                ModelDrivenIntro(
                    userCache = UserCache(
                        AppNetworkImpl(context, Dispatchers.Unconfined),
                        Dispatchers.Unconfined
                    ),
                    root(null)
                )
            }
        }
    }
}
