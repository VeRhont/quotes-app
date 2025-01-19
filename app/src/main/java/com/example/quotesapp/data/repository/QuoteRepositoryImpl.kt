package com.example.quotesapp.data.repository

import com.example.quotesapp.data.remote.QuotesApi
import com.example.quotesapp.data.remote.dto.QuotesDto
import com.example.quotesapp.domain.repository.QuoteRepository
import javax.inject.Inject


class QuoteRepositoryImpl @Inject constructor(
    private val api: QuotesApi
) : QuoteRepository {

    override suspend fun getQuote(): QuotesDto {
        return api.getQuote().first()
    }
}