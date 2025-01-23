package com.example.quotesapp.presentation.main_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.quotesapp.presentation.main_screen.MainScreenState
import com.example.quotesapp.presentation.theme.AuthorColor
import com.example.quotesapp.presentation.theme.BackgroundColor
import com.example.quotesapp.presentation.theme.BackgroundColorDark
import com.example.quotesapp.presentation.theme.QuoteColor
import com.example.quotesapp.presentation.theme.kleeOneFontFamily


@Composable
fun Photo(photoState: MainScreenState, quoteState: MainScreenState) {

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
                        BackgroundColorDark,
                        BackgroundColor
                    )
                )
            )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(80.dp, 80.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        quoteState.quote?.let {

            Text(
                text = "\"${it.quote}\"",
                color = QuoteColor,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontFamily = kleeOneFontFamily,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = it.author,
                color = AuthorColor,
                fontSize = 18.sp,
                fontFamily = kleeOneFontFamily,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}