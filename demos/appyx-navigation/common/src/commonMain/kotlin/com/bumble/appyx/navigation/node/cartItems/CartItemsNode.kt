package com.bumble.appyx.navigation.node.cartItems

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.navigator.LocalNavigator
import com.bumble.appyx.navigation.node.Node
import com.bumble.appyx.navigation.node.cakes.model.Cart

class CartItemsNode(
    buildContext: BuildContext,
    private val cart: Cart,
) : Node(
    buildContext = buildContext,
) {

    @Composable
    override fun View(modifier: Modifier) {
        val cartItems = cart.items.collectAsState(emptyMap())
        if (cartItems.value.isEmpty()) {
            CartEmptyContent()
        }
    }

    @Composable
    private fun CartEmptyContent() {
        val navigator = LocalNavigator.current
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Your cart is empty",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.requiredHeight(16.dp))
            Button(onClick = {
                navigator.goToCakes()
            }) {
                Text("Get some cakes!")
            }
        }
    }
}

