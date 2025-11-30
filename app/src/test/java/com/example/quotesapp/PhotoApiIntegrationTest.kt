package com.example.quotesapp

import com.example.quotesapp.data.remote.UnsplashApi
import com.example.quotesapp.data.repository.PhotoRepositoryImpl
import com.example.quotesapp.domain.repository.PhotoRepository
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PhotoApiIntegrationTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var unsplashApi: UnsplashApi
    private lateinit var photoRepository: PhotoRepository

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        unsplashApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UnsplashApi::class.java)

        photoRepository = PhotoRepositoryImpl(unsplashApi)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getPhotoFromApi should return photo when API returns successful response`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(MOCK_RESPONSE)
                .addHeader("Content-Type", "application/json")
        )

        val result = photoRepository.getPhoto(
            collections = "627564",
            orientation = "portrait",
        )

        assertEquals("test_photo_123", result.id)
        assertEquals("https://test.com/regular.jpg", result.urls.regular)
        assertEquals("Test photo description", result.description)

        val request = mockWebServer.takeRequest()

        assertTrue(request.path!!.contains("/photos/random"))
        assertTrue(request.path!!.contains("collections=627564"))
        assertTrue(request.path!!.contains("orientation=portrait"))
    }

    @Test
    fun `getPhotoFromApi should throw exception when API returns server error`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error")
        )

        try {
            photoRepository.getPhoto(
                collections = "627564",
                orientation = "portrait",
            )
            assertTrue("Expected HttpException to be thrown", false)
        } catch (e: Exception) {
            assertTrue(e is retrofit2.HttpException)
            assertEquals(500, (e as retrofit2.HttpException).code())
        }
    }

    private companion object {

        private val MOCK_RESPONSE = """
            {
                "id": "test_photo_123",
                "urls": {
                    "raw": "https://test.com/raw.jpg",
                    "full": "https://test.com/full.jpg", 
                    "regular": "https://test.com/regular.jpg",
                    "small": "https://test.com/small.jpg",
                    "thumb": "https://test.com/thumb.jpg"
                },
                "description": "Test photo description",
                "alt_description": "Test alt description"
            }
        """.trimIndent()
    }
}
