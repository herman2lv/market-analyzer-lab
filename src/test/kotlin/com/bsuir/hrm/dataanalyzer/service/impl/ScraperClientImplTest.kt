package com.bsuir.hrm.dataanalyzer.service.impl

import com.bsuir.hrm.dataanalyzer.service.dto.scraper.CategoryPageableDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import java.nio.file.Files
import java.nio.file.Path

@ExtendWith(MockitoExtension::class)
internal class ScraperClientImplTest {

    private lateinit var client: ScraperClientImpl

    @Mock
    private lateinit var restTemplate: RestTemplate
    private val jsonMapper: ObjectMapper = ObjectMapper()
    private val productsUrl: String = "https://catalog.onliner.by/sdapi/catalog.api/search/"
    private val pricesUrl: String = "https://catalog.api.onliner.by/products/"
    private val dateTimePattern: String = "yyyy-MM"
    private val category = "mobile"
    private val page = 1
    private val productKey = "x9a6128grn"
    private val productFilter = "price[from]=0.00"

    @BeforeEach
    fun setUp() {

        //products
        val urlGetProducts = "$productsUrl$category?page=$page&$productFilter"
        val productsContent = String(Files.readAllBytes(Path.of("src/test/resources/products.json")))
//        Mockito.`when`(restTemplate.getForEntity(urlGetProducts, String::class.java)).thenReturn(ResponseEntity(productsContent, HttpStatusCode.valueOf(200)))

        //statistics
        val urlGetStatistics = "$pricesUrl${productKey}/prices-history?period=12m"
        val productContent = String(Files.readAllBytes(Path.of("src/test/resources/product.json")))
//        Mockito.`when`(restTemplate.getForEntity(urlGetStatistics, String::class.java)).thenReturn(ResponseEntity(productContent, HttpStatusCode.valueOf(200)))

        //pageable
        val urlGetPageable = "$productsUrl$category?$productFilter"
        Mockito.`when`(restTemplate.getForEntity(urlGetPageable, String::class.java)).thenReturn(ResponseEntity(productsContent, HttpStatusCode.valueOf(200)))


        client = ScraperClientImpl(restTemplate, jsonMapper, productsUrl, pricesUrl, dateTimePattern)
    }

    @Test
    fun getProducts() {
        val result: CategoryPageableDto = client.getPageable("mobile")
        val expected = CategoryPageableDto("mobile", 3438, 30, 115)
        assertEquals(expected, result)
    }

    @Test
    fun getPriceStatistics() {
    }

    @Test
    fun getPageable() {
    }
}
