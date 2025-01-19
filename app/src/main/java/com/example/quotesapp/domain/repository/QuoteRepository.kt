package com.example.quotesapp.domain.repository

import com.example.quotesapp.data.remote.dto.QuotesDto


interface QuoteRepository {

    suspend fun getQuote(): QuotesDto

}