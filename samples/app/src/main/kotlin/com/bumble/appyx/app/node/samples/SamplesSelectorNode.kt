package com.bumble.appyx.app.node.samples

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.composable.ChildRenderer
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node

class SamplesSelectorNode(
    buildContext: BuildContext,
    private val outputFunc: (Output) -> Unit
) : Node(
    buildContext = buildContext
) {

    sealed class Output {
        object OpenCardsExample : Output()
        object OpenOnboarding : Output()
        object OpenComposeNavigation : Output()
        object OpenInsideTheBackStack : Output()
    }

    @Composable
    override fun View(modifier: Modifier) {
        val decorator: @Composable (child: ChildRenderer) -> Unit = remember {
            { childRenderer ->
                ScaledLayout {
                    childRenderer.invoke()
                }
            }
        }
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)

        ) {
            item {
                CardItem(decorator)
            }
            item {
                WhatIsAppyxItem(decorator)
            }
            item {
                ComposeNavigationItem(decorator)
            }
            item {
                InsideTheBackStackItem(decorator)
            }
        }
    }

    @Composable
    private fun InsideTheBackStackItem(decorator: @Composable (child: ChildRenderer) -> Unit) {
        SampleItem(
            title = "Inside the backstack",
            subtitle = "See how the backstack behaves when operations are performed",
            modifier = Modifier
                .fillMaxSize(),
            onClick = { outputFunc(Output.OpenInsideTheBackStack) },
        ) {}
    }

    @Composable
    private fun ComposeNavigationItem(decorator: @Composable (child: ChildRenderer) -> Unit) {
        SampleItem(
            title = "Compose Navigation",
            subtitle = "See Appyx nodes interact with Jetpack Compose Navigation library",
            modifier = Modifier
                .fillMaxSize(),
            onClick = { outputFunc(Output.OpenComposeNavigation) },
        ) {}
    }

    @Composable
    private fun WhatIsAppyxItem(decorator: @Composable (child: ChildRenderer) -> Unit) {
        SampleItem(
            title = "What is Appyx?",
            subtitle = "Explore some of the main ideas of Appyx in a set of slides",
            modifier = Modifier
                .fillMaxSize(),
            onClick = { outputFunc(Output.OpenOnboarding) },
        ) {}
    }

    @Composable
    private fun CardItem(decorator: @Composable (child: ChildRenderer) -> Unit) {
        SampleItem(
            title = "Dating cards NavModel",
            subtitle = "Swipe right on the NavModel concept",
            onClick = { outputFunc(Output.OpenCardsExample) },
        ) {}
    }
}
