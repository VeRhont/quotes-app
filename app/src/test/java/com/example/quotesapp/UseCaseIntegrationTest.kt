package com.example.quotesapp

import com.example.quotesapp.common.Resource
import com.example.quotesapp.data.remote.UnsplashApi
import com.example.quotesapp.data.repository.PhotoRepositoryImpl
import com.example.quotesapp.domain.model.Photo
import com.example.quotesapp.domain.repository.PhotoRepository
import com.example.quotesapp.domain.use_case.GetPhotoUseCase
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UseCaseIntegrationTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var unsplashApi: UnsplashApi
    private lateinit var photoRepository: PhotoRepository
    private lateinit var getPhotoUseCase: GetPhotoUseCase

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
        getPhotoUseCase = GetPhotoUseCase(photoRepository)
    }

    @Test
    fun `getPhotoUseCase should return success resource when repository returns photo`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(SUCCESS_PHOTO_RESPONSE)
                .addHeader("Content-Type", "application/json")
        )

        val resultFlow = getPhotoUseCase()
        val results = mutableListOf<Resource<Photo>>()
        resultFlow.collect { results.add(it) }

        assertEquals(2, results.size)
        assertTrue(results[0] is Resource.Loading)
        assertTrue(results[1] is Resource.Success)

        val photo = (results[1] as Resource.Success).data

        assertEquals("https://test.com/small2.jpg", photo?.url)
        assertEquals("Test photo for use case", photo?.description)

        val request = mockWebServer.takeRequest()
        assertTrue(request.path!!.contains("collections=627564"))
        assertTrue(request.path!!.contains("orientation=portrait"))
    }

    @Test
    fun `getPhotoUseCase should return error resource when repository throws HttpException`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(404)
                .setBody("Not Found")
        )

        val resultFlow = getPhotoUseCase()
        val results = mutableListOf<Resource<Photo>>()
        resultFlow.collect { results.add(it) }

        assertEquals(2, results.size)

        assertTrue(results[0] is Resource.Loading)

        assertTrue(results[1] is Resource.Error)
        val errorMessage = (results[1] as Resource.Error).message
        assertNotNull(errorMessage)
        assertTrue(errorMessage!!.isNotEmpty())
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private companion object {
        private val SUCCESS_PHOTO_RESPONSE = """
            {
                "id": "test_photo_456",
                "urls": {
                    "raw": "https://test.com/raw2.jpg",
                    "full": "https://test.com/full2.jpg", 
                    "regular": "https://test.com/regular2.jpg",
                    "small": "https://test.com/small2.jpg",
                    "thumb": "https://test.com/thumb2.jpg"
                },
                "description": "Test photo for use case",
                "alt_description": "Test alt for use case"
            }
        """.trimIndent()
    }
}
