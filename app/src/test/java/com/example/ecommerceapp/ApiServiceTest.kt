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
import java.io.FileNotFoundException
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

    /**
     * Enqueues a mock response from a given JSON string content.
     * @param content The JSON string to be used as the response body.
     */
    private fun enqueueMockResponseFromString(content: String) {
        val mockResponse = MockResponse()
        mockResponse.setBody(content)
        server.enqueue(mockResponse)
    }

    @Test
    fun `getProducts request path is correct`() = runTest {
        // Given
        val productsJson = """
            {
              "products": [],
              "total": 0,
              "skip": 0,
              "limit": 0
            }
        """.trimIndent()
        enqueueMockResponseFromString(productsJson)

        // When
        apiService.getProducts()

        // Then
        val request = server.takeRequest()
        assertThat(request.path).isEqualTo("/products?limit=10&skip=5")
    }

    @Test
    fun `getProducts returns correct product list on success`() = runTest {
        // Given
        val productsJson = """
            {
              "products": [
                {
                  "id": 1,
                  "title": "iPhone 9",
                  "description": "An apple mobile which is nothing like apple",
                  "price": 549,
                  "brand": "Apple",
                  "category": "smartphones"
                },
                {
                  "id": 2,
                  "title": "iPhone X",
                  "description": "SIM-Free, Model A19211 6.5-inch Super Retina HD display with OLED technology",
                  "price": 899,
                  "brand": "Apple",
                  "category": "smartphones"
                }
              ],
              "total": 2,
              "skip": 0,
              "limit": 2
            }
        """.trimIndent()
        enqueueMockResponseFromString(productsJson)

        // When
        val responseBody = apiService.getProducts()

        // Then
        assertThat(responseBody).isNotNull()
        assertThat(responseBody.products).hasSize(2)
        assertThat(responseBody.products[0].title).isEqualTo("iPhone 9")
        assertThat(responseBody.products[1].brand).isEqualTo("Apple")
    }

    @Test
    fun `getCategoryList request path is correct`() = runTest {
        // Given
        val categoriesJson = """
            [
              "smartphones",
              "laptops",
              "fragrances"
            ]
        """.trimIndent()
        enqueueMockResponseFromString(categoriesJson)

        // When
        apiService.getCategoryList()

        // Then
        val request = server.takeRequest()
        assertThat(request.path).isEqualTo("/products/category-list")
    }

    @Test
    fun `getCategoryList returns correct list of categories on success`() = runTest {
        // Given
        val categoriesJson = """
            [
              "smartphones",
              "laptops",
              "fragrances"
            ]
        """.trimIndent()
        enqueueMockResponseFromString(categoriesJson)

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