package com.example.quotesapp.domain.use_case

import com.example.quotesapp.common.Resource
import com.example.quotesapp.data.remote.dto.toQuote
import com.example.quotesapp.domain.model.Quote
import com.example.quotesapp.domain.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class GetQuoteUseCase @Inject constructor(
    private val repository: QuoteRepository
) {
    operator fun invoke(): Flow<Resource<Quote>> = flow {
        try {
            emit(Resource.Loading<Quote>())
            val quote = repository.getQuote().toQuote()
            emit(Resource.Success<Quote>(quote))
        } catch (e: HttpException) {
            emit(Resource.Error<Quote>(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error<Quote>("Couldn't reach server. Check your Internet connection"))
        }
    }
}