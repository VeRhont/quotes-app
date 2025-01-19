package com.example.quotesapp.data.remote

import com.example.quotesapp.BuildConfig
import com.example.quotesapp.data.remote.dto.QuotesDto
import retrofit2.http.GET
import retrofit2.http.Headers


interface QuotesApi {

    @Headers("X-Api-Key: ${BuildConfig.QUOTE_API_KEY}")
    @GET("/v1/quotes")
    suspend fun getQuote(): List<QuotesDto>
}