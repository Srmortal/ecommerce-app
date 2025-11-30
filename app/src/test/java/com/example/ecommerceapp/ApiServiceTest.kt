package com.example.ecommerceapp

import com.example.ecommerceapp.data.Remote.ApiService
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStreamReader

class ApiServiceTest {

    private lateinit var server: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        server = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(server.url("/")) // Use the mock server's URL
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(ApiService::class.java)
    }

    private fun enqueueMockResponse(fileName: String) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val source = InputStreamReader(inputStream).readText()
        val mockResponse = MockResponse()
        mockResponse.setBody(source)
        server.enqueue(mockResponse)
    }

    @Test
    fun `getProducts request path is correct`() = runTest {
        // Given
        enqueueMockResponse("success_products_response.json")

        // When
        apiService.getProducts()

        // Then
        val request = server.takeRequest()
        assertThat(request.path).isEqualTo("/products")
    }

    @Test
    fun `getProducts returns correct product list on success`() = runTest {
        // Given
        enqueueMockResponse("success_products_response.json")

        // When
        val responseBody = apiService.getProducts()

        // Then
        assertThat(responseBody).isNotNull()
        assertThat(responseBody.products).hasSize(2)
        assertThat(responseBody.products[0].title).isEqualTo("iPhone 9")
        assertThat(responseBody.products[1].brand).isEqualTo("Apple")
    }

    @Test
    fun `getCategories request path is correct`() = runTest {
        // Given
        enqueueMockResponse("success_categories_response.json")

        // When
        apiService.getCategoryList()

        // Then
        val request = server.takeRequest()
        assertThat(request.path).isEqualTo("/products/category-list")
    }

    @Test
    fun `getCategories returns correct list of categories on success`() = runTest {
        // Given
        enqueueMockResponse("success_categories_response.json")

        // When
        val responseBody = apiService.getCategoryList()

        // Then
        assertThat(responseBody).isNotNull()
        assertThat(responseBody).hasSize(3)
        assertThat(responseBody).contains("laptops")
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}