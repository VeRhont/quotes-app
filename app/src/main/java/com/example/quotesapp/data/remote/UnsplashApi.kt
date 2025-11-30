package com.example.quotesapp.data.remote

import com.example.quotesapp.BuildConfig
import com.example.quotesapp.data.remote.dto.PhotoDto
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface UnsplashApi {

    @Headers("Authorization: Client-ID ${BuildConfig.UNSPLASH_API_KEY}")
    @GET("/photos/random")
    suspend fun getPhoto(
        @Query("collections") collections: String,
        @Query("orientation") orientation: String,
    ): PhotoDto

}
