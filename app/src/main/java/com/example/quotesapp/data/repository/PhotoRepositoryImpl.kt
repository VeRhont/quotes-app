package com.example.quotesapp.data.repository

import com.example.quotesapp.data.remote.UnsplashApi
import com.example.quotesapp.data.remote.dto.PhotoDto
import com.example.quotesapp.domain.repository.PhotoRepository
import javax.inject.Inject


class PhotoRepositoryImpl @Inject constructor(
    private val api: UnsplashApi
) : PhotoRepository {

    override suspend fun getPhoto(orientation: String): PhotoDto {
        return api.getPhoto(orientation)
    }
}