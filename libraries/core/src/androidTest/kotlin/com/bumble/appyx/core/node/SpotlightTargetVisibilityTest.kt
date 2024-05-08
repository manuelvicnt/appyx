package com.bumble.appyx.core.node

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.AppyxTestScenario
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.navmodel.spotlight.Spotlight
import com.bumble.appyx.navmodel.spotlight.operation.activate
import kotlinx.parcelize.Parcelize
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SpotlightTargetVisibilityTest {

    private lateinit var spotlight: Spotlight<NavTarget>

    var nodeOneTargetVisibilityState: Boolean = false
    var nodeTwoTargetVisibilityState: Boolean = false
    var nodeThreeTargetVisibilityState: Boolean = false

    var nodeFactory: (buildContext: BuildContext) -> TestParentNode = {
        TestParentNode(buildContext = it, spotlight = spotlight)
    }

    @get:Rule
    val rule = AppyxTestScenario { buildContext ->
        nodeFactory(buildContext)
    }

    @Test
    fun `GIVEN_spotlight_WHEN_operations_called_THEN_child_nodes_have_correct_targetVisibility_state`() {
        val initialActiveIndex = 2
        createSpotlight(initialActiveIndex)
        rule.start()

        assertTrue(nodeThreeTargetVisibilityState)

        spotlight.activate(1)
        rule.waitForIdle()

        assertFalse(nodeOneTargetVisibilityState)
        assertTrue(nodeTwoTargetVisibilityState)
        assertFalse(nodeThreeTargetVisibilityState)

        spotlight.activate(0)
        rule.waitForIdle()

        assertTrue(nodeOneTargetVisibilityState)
        assertFalse(nodeTwoTargetVisibilityState)
        assertFalse(nodeThreeTargetVisibilityState)
    }


    private fun createSpotlight(initialActiveIndex: Int) {
        spotlight = Spotlight(
            savedStateMap = null,
            items = listOf(NavTarget.NavTarget1, NavTarget.NavTarget2, NavTarget.NavTarget3),
            initialActiveIndex = initialActiveIndex
        )
    }

    @Parcelize
    sealed class NavTarget : Parcelable {

        data object NavTarget1 : NavTarget()

        data object NavTarget2 : NavTarget()

        data object NavTarget3 : NavTarget()
    }

    inner class TestParentNode(
        buildContext: BuildContext,
        val spotlight: Spotlight<NavTarget>,
    ) : ParentNode<NavTarget>(
        buildContext = buildContext,
        navModel = spotlight
    ) {

        override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node =
            when (navTarget) {
                NavTarget.NavTarget1 -> node(buildContext) {
                    nodeOneTargetVisibilityState = LocalNodeTargetVisibility.current
                }

                NavTarget.NavTarget2 -> node(buildContext) {
                    nodeTwoTargetVisibilityState = LocalNodeTargetVisibility.current
                }
                NavTarget.NavTarget3 -> node(buildContext) {
                    nodeThreeTargetVisibilityState = LocalNodeTargetVisibility.current
                }
            }

        @Composable
        override fun View(modifier: Modifier) {
            Children(navModel)
        }
    }

}
