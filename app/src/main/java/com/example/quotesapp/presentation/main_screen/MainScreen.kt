package com.example.quotesapp.presentation.main_screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter


@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val state = viewModel.state.value
    val photoState = viewModel.photoState.value

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        photoState.photo?.let {
            Image(
                painter = rememberAsyncImagePainter(it.url),
                contentDescription = it.description,
                modifier = Modifier.size(220.dp)
            )
        }

        state.quote?.let {
            Text(
                text = it.quote
            )
            Text(
                text = it.author
            )
        }

        if (state.error.isNotBlank()) {
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
            )
        }

        if (state.isLoading) {
            CircularProgressIndicator()
        }
    }
}