package com.example.quotesapp.presentation.main_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotesapp.common.Resource
import com.example.quotesapp.domain.use_case.GetPhotoUseCase
import com.example.quotesapp.domain.use_case.GetQuoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getQuoteUseCase: GetQuoteUseCase,
    private val getPhotoUseCase: GetPhotoUseCase
) : ViewModel() {

    private val _state = mutableStateOf(MainScreenState())
    val state: State<MainScreenState> = _state

    private val _photoState = mutableStateOf(MainScreenState())
    val photoState: State<MainScreenState> = _photoState

    init {
        getPhoto()
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

    private fun getPhoto() {
        getPhotoUseCase().onEach { result ->

            when (result) {
                is Resource.Error -> {
                    _photoState.value = MainScreenState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }

                is Resource.Loading -> {
                    _photoState.value = MainScreenState(isLoading = true)
                }

                is Resource.Success -> {
                    Log.d("SUCCESS", result.data.toString())
                    _photoState.value = MainScreenState(photo = result.data)
                }
            }
        }.launchIn(viewModelScope)
    }
}