package com.example.quotesapp.presentation.main_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotesapp.common.Resource
import com.example.quotesapp.domain.use_case.GetQuoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getQuoteUseCase: GetQuoteUseCase,
) : ViewModel() {

    private val _state = mutableStateOf(MainScreenState())
    val state: State<MainScreenState> = _state

    init {
        getQuote()
    }

    private fun getQuote() {
        getQuoteUseCase().onEach { result ->

            when (result) {
                is Resource.Error -> {
                    _state.value = MainScreenState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }

                is Resource.Loading -> {
                    _state.value = MainScreenState(isLoading = true)
                }

                is Resource.Success -> {
                    _state.value = MainScreenState(quote = result.data)
                }
            }

        }.launchIn(viewModelScope)
    }
}