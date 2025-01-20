package com.example.quotesapp.presentation.main_screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.quotesapp.presentation.theme.AuthorColor
import com.example.quotesapp.presentation.theme.BackgroundColor
import com.example.quotesapp.presentation.theme.BackgroundColorDark
import com.example.quotesapp.presentation.theme.QuoteColor


@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val state = viewModel.state.value
    val photoState = viewModel.photoState.value

    Box(
        modifier = modifier
            .fillMaxSize().background(Color.Red),
        contentAlignment = Alignment.Center
    ) {

        photoState.photo?.let {
            Image(
                painter = rememberAsyncImagePainter(it.url),
                contentDescription = it.description,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            BackgroundColor,
                            BackgroundColorDark
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(60.dp, 100.dp),
//                .background(Color.Red),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            state.quote?.let {
                Text(
                    text = it.quote,
                    color = QuoteColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = it.author,
                    color = AuthorColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
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