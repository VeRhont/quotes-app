package com.example.quotesapp.presentation.main_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quotesapp.presentation.main_screen.components.Photo
import com.example.quotesapp.presentation.theme.ButtonColor
import com.example.quotesapp.presentation.theme.QuoteColor
import kotlinx.coroutines.launch


@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val quoteState = viewModel.quoteState.value
    val photoState = viewModel.photoState.value

    val graphicsLayer = rememberGraphicsLayer()
    val coroutineScope = rememberCoroutineScope()
    val contentResolver = LocalContext.current.contentResolver

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Box(modifier = Modifier
            .drawWithContent {
                graphicsLayer.record {
                    this@drawWithContent.drawContent()
                }
                drawLayer(graphicsLayer)
            }
        ) {
            Photo(photoState, quoteState)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                FloatingActionButton(
                    onClick = {
                        viewModel.updatePhoto()
                    },
                    containerColor = ButtonColor,
                    contentColor = QuoteColor
                ) {
                    Icon(Icons.Filled.Refresh, "New Photo")
                }

                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            val bitmap = graphicsLayer
                                .toImageBitmap()
                                .asAndroidBitmap()

                            viewModel.savePhoto(
                                contentResolver = contentResolver,
                                bitmap = bitmap
                            )
                        }
                    },
                    containerColor = ButtonColor,
                    contentColor = QuoteColor
                ) {
                    Icon(Icons.Filled.Save, "Save")
                }
            }
        }

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
