package com.bumble.appyx.core.node

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.AppyxTestScenario
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.operation.pop
import com.bumble.appyx.navmodel.backstack.operation.push
import kotlinx.parcelize.Parcelize
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class BackStackTargetVisibilityTest {

    private val backStack = BackStack<NavTarget>(
        savedStateMap = null,
        initialElement = NavTarget.NavTarget1
    )

    var nodeOneTargetVisibilityState: Boolean = false
    var nodeTwoTargetVisibilityState: Boolean = false

    var nodeFactory: (buildContext: BuildContext) -> TestParentNode = {
        TestParentNode(buildContext = it, backStack = backStack)
    }

    @get:Rule
    val rule = AppyxTestScenario { buildContext ->
        nodeFactory(buildContext)
    }

    @Test
    fun `GIVEN_backStack_WHEN_operations_called_THEN_child_nodes_have_correct_targetVisibility_state`() {
        rule.start()
        assertTrue(nodeOneTargetVisibilityState)

        backStack.push(NavTarget.NavTarget2)
        rule.waitForIdle()

        assertFalse(nodeOneTargetVisibilityState)
        assertTrue(nodeTwoTargetVisibilityState)

        backStack.pop()
        rule.waitForIdle()

        assertFalse(nodeTwoTargetVisibilityState)
        assertTrue(nodeOneTargetVisibilityState)
    }


    @Parcelize
    sealed class NavTarget : Parcelable {

        data object NavTarget1 : NavTarget()

        data object NavTarget2 : NavTarget()
    }

    inner class TestParentNode(
        buildContext: BuildContext,
        val backStack: BackStack<NavTarget>,
    ) : ParentNode<NavTarget>(
        buildContext = buildContext,
        navModel = backStack
    ) {

        override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node =
            when (navTarget) {
                NavTarget.NavTarget1 -> node(buildContext) {
                    nodeOneTargetVisibilityState = LocalNodeTargetVisibility.current
                }

                NavTarget.NavTarget2 -> node(buildContext) {
                    nodeTwoTargetVisibilityState = LocalNodeTargetVisibility.current
                }
            }

        @Composable
        override fun View(modifier: Modifier) {
            Children(navModel)
        }
    }

}
