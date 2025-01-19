package com.example.quotesapp.presentation.main_screen

import com.example.quotesapp.domain.model.Quote

data class MainScreenState(
    val isLoading: Boolean = false,
    val error: String = "",
    val quote: Quote? = null,
)