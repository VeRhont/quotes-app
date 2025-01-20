package com.example.quotesapp.di

import com.example.quotesapp.common.Constants.QUOTES_API_BASE_URL
import com.example.quotesapp.common.Constants.UNSPLASH_API_BASE_URL
import com.example.quotesapp.data.remote.QuotesApi
import com.example.quotesapp.data.remote.UnsplashApi
import com.example.quotesapp.data.repository.PhotoRepositoryImpl
import com.example.quotesapp.data.repository.QuoteRepositoryImpl
import com.example.quotesapp.domain.repository.PhotoRepository
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
            .baseUrl(QUOTES_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuotesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideQuoteRepository(api: QuotesApi): QuoteRepository {
        return QuoteRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideUnsplashApi(): UnsplashApi {
        return Retrofit.Builder()
            .baseUrl(UNSPLASH_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UnsplashApi::class.java)
    }

    @Provides
    @Singleton
    fun providePhotoRepository(api: UnsplashApi): PhotoRepository {
        return PhotoRepositoryImpl(api)
    }
}