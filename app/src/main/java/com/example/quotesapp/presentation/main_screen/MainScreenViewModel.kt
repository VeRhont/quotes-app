package com.example.quotesapp.presentation.main_screen

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotesapp.common.Resource
import com.example.quotesapp.domain.model.Photo
import com.example.quotesapp.domain.use_case.GetPhotoUseCase
import com.example.quotesapp.domain.use_case.GetQuoteUseCase
import com.example.quotesapp.domain.use_case.SavePhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getQuoteUseCase: GetQuoteUseCase,
    private val getPhotoUseCase: GetPhotoUseCase,
    private val savePhotoUseCase: SavePhotoUseCase,
) : ViewModel() {

    private val _quoteState = mutableStateOf(MainScreenState())
    val quoteState: State<MainScreenState> = _quoteState

    private val _photoState = mutableStateOf(MainScreenState())
    val photoState: State<MainScreenState> = _photoState

    init {
        getPhoto()
    }

    private fun getQuote() {
        getQuoteUseCase().onEach { result ->

            when (result) {
                is Resource.Error -> {
                    _quoteState.value = MainScreenState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }

                is Resource.Loading -> {
                    _quoteState.value = MainScreenState(isLoading = true)
                }

                is Resource.Success -> {
                    _quoteState.value = MainScreenState(quote = result.data)
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
                    _photoState.value = MainScreenState(photo = result.data)
                    getQuote()
                }
            }
        }.launchIn(viewModelScope)
    }

    fun trySavePhoto(contentResolver: ContentResolver, bitmap: Bitmap): Boolean {
        return savePhotoUseCase(contentResolver, bitmap)
    }

    fun loadNewPhoto() {
        getPhoto()
    }

    fun photo(uri: Uri) {
        _photoState.value = MainScreenState(
            photo = Photo(url = uri.toString(), description = "")
        )
    }
}