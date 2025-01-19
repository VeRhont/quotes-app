package com.example.quotesapp.di

import com.example.quotesapp.common.Constants.BASE_URL
import com.example.quotesapp.data.remote.QuotesApi
import com.example.quotesapp.data.repository.QuoteRepositoryImpl
import com.example.quotesapp.domain.repository.QuoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideQuotesApi(): QuotesApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuotesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideQuoteRepository(api: QuotesApi): QuoteRepository {
        return QuoteRepositoryImpl(api)
    }
}