package com.example.quotesapp.domain.repository

import com.example.quotesapp.data.remote.dto.PhotoDto


interface PhotoRepository {

    suspend fun getPhoto(orientation: String): PhotoDto

}