package com.example.quotesapp

import com.example.quotesapp.data.remote.QuotesApi
import com.example.quotesapp.data.repository.QuoteRepositoryImpl
import com.example.quotesapp.domain.repository.QuoteRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuoteApiIntegrationTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var quotesApi: QuotesApi
    private lateinit var quoteRepository: QuoteRepository

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        quotesApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuotesApi::class.java)

        quoteRepository = QuoteRepositoryImpl(quotesApi)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getQuoteFromApi should return quote when API returns successful response`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(SUCCESS_RESPONSE)
                .addHeader("Content-Type", "application/json")
        )

        val result = quoteRepository.getQuote()

        assertEquals(SUCCESS_QUOTE, result.quote)
        assertEquals(SUCCESS_AUTHOR, result.author)
    }

    @Test
    fun `getQuoteFromApi should return first quote when API returns multiple quotes`() =
        runBlocking {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(MULTIPLE_QUOTES_RESPONSE)
            )

            val result = quoteRepository.getQuote()

            assertEquals(FIRST_QUOTE, result.quote)
            assertEquals(FIRST_AUTHOR, result.author)
        }

    @Test
    fun `getQuoteFromApi should throw exception when API returns server error`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error")
        )

        try {
            quoteRepository.getQuote()
            assertTrue("Expected HttpException to be thrown", false)
        } catch (e: Exception) {
            assertTrue(e is retrofit2.HttpException)
            assertEquals(500, (e as retrofit2.HttpException).code())
        }
    }

    private companion object {
        private const val SUCCESS_QUOTE =
            "Жизнь - это то, что происходит с тобой, пока ты строишь другие планы."
        private const val SUCCESS_AUTHOR = "Джон Леннон"
        private const val FIRST_QUOTE = "First quote"
        private const val FIRST_AUTHOR = "First Author"
        private const val SECOND_QUOTE = "Second quote"
        private const val SECOND_AUTHOR = "Second Author"

        private val SUCCESS_RESPONSE = """
            [
                {
                    "quote": "$SUCCESS_QUOTE",
                    "author": "$SUCCESS_AUTHOR",
                    "category": "life"
                }
            ]
        """.trimIndent()

        private val MULTIPLE_QUOTES_RESPONSE = """
            [
                {
                    "quote": "$FIRST_QUOTE",
                    "author": "$FIRST_AUTHOR",
                    "category": "test"
                },
                {
                    "quote": "$SECOND_QUOTE", 
                    "author": "$SECOND_AUTHOR",
                    "category": "test"
                }
            ]
        """.trimIndent()
    }
}
