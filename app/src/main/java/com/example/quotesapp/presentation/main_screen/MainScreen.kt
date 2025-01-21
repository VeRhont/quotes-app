package com.example.quotesapp.presentation.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quotesapp.presentation.main_screen.components.Photo
import kotlinx.coroutines.launch


@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val quoteState = viewModel.quoteState.value
    val photoState = viewModel.photoState.value

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

    Box(
        modifier = Modifier
            .drawWithContent {
                graphicsLayer.record {
                    this@drawWithContent.drawContent()
                }
                drawLayer(graphicsLayer)
            }
            .clickable {
                coroutineScope.launch {
                    val bitmap = graphicsLayer
                        .toImageBitmap()
                        .asAndroidBitmap()

                    viewModel.savePhoto(
                        context = context,
                        bitmap = bitmap
                    )
                }
            }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Red),
            contentAlignment = Alignment.Center
        ) {

            Photo(photoState, quoteState)

            if (quoteState.error.isNotBlank()) {
                Text(
                    text = quoteState.error,
                    color = MaterialTheme.colorScheme.error,
                )
            }

            if (photoState.isLoading) {
                CircularProgressIndicator()
            }

            if (quoteState.isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}
