package com.bumble.appyx.app.node.samples

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumble.appyx.app.ui.AppyxSampleAppTheme

@Composable
fun SampleItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp,
    ) {
        Row(
            Modifier
                .padding(16.dp)
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium
            ) {
                content()
            }
            Spacer(modifier = Modifier.size(16.dp))
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Start
                )
                Spacer(
                    modifier = Modifier.size(8.dp)
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview
@Composable
private fun SampleItemPreview() {
    AppyxSampleAppTheme {
        SampleItem(
            title = "What is Appyx?",
            subtitle = "Explore some of the main ideas of Appyx in a set of slides",
            onClick = {},
            modifier = Modifier
        ) {}
    }
}
