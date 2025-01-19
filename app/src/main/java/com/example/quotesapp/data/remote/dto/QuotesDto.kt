package com.example.quotesapp.data.remote.dto

import com.example.quotesapp.domain.model.Quote


data class QuotesDto(
    val quote: String,
    val author: String,
    val category: String
)


fun QuotesDto.toQuote(): Quote {
    return Quote(
        quote = quote,
        author = author
    )
}