package com.bumble.appyx.app.node.slideshow.slide.modeldriven

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
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
class Intro @AssistedInject constructor(
    private val introStateHolder: IntroStateHolder,
    @Assisted buildContext: BuildContext,
) : Node(
    buildContext = buildContext
) {

    @AssistedFactory
    interface IntroNodeFactory {
        fun create(buildContext: BuildContext): Intro
    }

    @Composable
    override fun View(modifier: Modifier) {
        LaunchedEffect(Unit) {
            Log.d("MANUEL_TAG", "Intro UserCache=${introStateHolder.userCache}")
            Log.d(
                "MANUEL_TAG",
                "Intro UserCache's AppNetwork=${introStateHolder.userCache.appNetwork}"
            )
            introStateHolder.setUserId()
        }

        Page(
            modifier = modifier,
            title = "Hi there!",
            body = "Appyx is a model-driven navigation library built with love on top of Jetpack Compose."
        ) {
            val image: Painter = painterResource(id = R.drawable.appyx)
            Image(
                painter = image,
                contentDescription = "logo",
                modifier = Modifier
                    .fillMaxSize(0.65f)
                    .align(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
@ExperimentalUnitApi
fun IntroScreenPreview() {
    AppyxSampleAppTheme(darkTheme = false) {
        PreviewContent()
    }
}

@Preview
@Composable
@ExperimentalUnitApi
fun IntroScreenPreviewDark() {
    AppyxSampleAppTheme(darkTheme = true) {
        PreviewContent()
    }
}

@Composable
@ExperimentalUnitApi
private fun PreviewContent() {
    val context = LocalContext.current
    Surface(color = MaterialTheme.colorScheme.background) {
        Box(Modifier.fillMaxSize()) {
            NodeHost(integrationPoint = IntegrationPointStub()) {
                Intro(
                    introStateHolder = IntroStateHolder(
                        UserCache(
                            AppNetworkImpl(context, Dispatchers.Unconfined),
                            Dispatchers.Unconfined
                        )
                    ),
                    root(null)
                )
            }
        }
    }
}
