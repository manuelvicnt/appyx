package com.bumble.appyx.sandbox.client.sharedelement

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.navigation.transition.sharedElement
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.samples.common.profile.Profile
import com.bumble.appyx.samples.common.profile.ProfileImage

class ProfileVerticalListNode(
    private val profileId: Int,
    private val onProfileClick: (Int) -> Unit,
    private val hasMovableContent: Boolean,
    buildContext: BuildContext
) : Node(buildContext) {

    @Composable
    override fun View(modifier: Modifier) {
        val state = rememberLazyListState(initialFirstVisibleItemIndex = profileId)
        LazyColumn(
            state = state,
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            repeat(10) { profileId ->
                item(key = profileId) {
                    if (hasMovableContent) {
                        SharedElementWithMovableContentContent(profileId)
                    } else {
                        SharedElementContent(profileId)
                    }
                }
            }
        }
    }


    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    private fun SharedElementContent(
        profileId: Int,
        modifier: Modifier = Modifier
    ) {
        Row(
            modifier = modifier
                .clickable {
                    onProfileClick(profileId)
                }
        ) {
            val profile = Profile.allProfiles[profileId]

            ProfileImage(
                Profile.allProfiles[profileId].drawableRes, modifier = Modifier
                    .requiredSize(64.dp)
                    .sharedElement(key = "$profileId image")
                    .clip(CircleShape)
            )
            Text(
                text = "${profile.name}, ${profile.age}",
                color = LocalContentColor.current,
                fontSize = 32.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .sharedElement(key = "$profileId text")
                    .padding(8.dp)
            )
        }
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    private fun SharedElementWithMovableContentContent(
        profileId: Int,
        modifier: Modifier = Modifier
    ) {
        Row(
            modifier = modifier
                .clickable {
                    onProfileClick(profileId)
                }
        ) {
            val profile = Profile.allProfiles[profileId]
            Box(
                modifier = Modifier
                    .requiredSize(64.dp)
                    .sharedElement(key = "$profileId image")
                    .clip(CircleShape)
            ) {
                ProfileImageWithCounterMovableContent(profileId)
            }
            Text(
                text = "${profile.name}, ${profile.age}",
                color = LocalContentColor.current,
                fontSize = 32.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .sharedElement(key = "$profileId text")
                    .padding(8.dp)
            )
        }
    }
}
