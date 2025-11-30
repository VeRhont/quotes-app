package com.example.quotesapp.data.remote.dto

import com.example.quotesapp.domain.model.Photo


data class PhotoDto(
    val id: String,
    val width: Int,
    val height: Int,
    val description: String? = null,
    val urls: UnsplashUrls
)


fun PhotoDto.toPhoto(): Photo {
    return Photo(
        url = urls.small,
        description = description.orEmpty()
    )
}