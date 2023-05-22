package com.bsuir.hrm.dataanalyzer.service.impl

import com.bsuir.hrm.dataanalyzer.data.entity.Money
import com.bsuir.hrm.dataanalyzer.data.entity.Money.Currency.BYN
import com.bsuir.hrm.dataanalyzer.service.dto.scraper.CategoryPageableDto
import com.bsuir.hrm.dataanalyzer.service.dto.scraper.PriceEntryDto
import com.bsuir.hrm.dataanalyzer.service.dto.scraper.PriceStatisticsDto
import com.bsuir.hrm.dataanalyzer.service.dto.scraper.ProductDto
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
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate

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
        Mockito.reset(restTemplate)
        client = ScraperClientImpl(restTemplate, jsonMapper, productsUrl, pricesUrl, dateTimePattern)
    }

    @Test
    fun getProducts() {
        val urlGetProducts = "$productsUrl$category?page=$page&$productFilter"
        val productsContent = String(Files.readAllBytes(Path.of("src/test/resources/products.json")))
        Mockito.`when`(restTemplate.getForEntity(urlGetProducts, String::class.java)).thenReturn(ResponseEntity(productsContent, HttpStatusCode.valueOf(200)))

        val result: List<ProductDto> = client.getProducts(category, page)
        println(result)
        assertEquals(30, result.size)
        val expected = ProductDto(3865677, "x9a6128grn", "HONOR X9a 6GB/128GB (изумрудный зеленый)", Money(BigDecimal.valueOf(840.0), BYN))
        assertEquals(expected, result[0])
    }

    @Test
    fun getPriceStatistics() {
        val urlGetStatistics = "$pricesUrl${productKey}/prices-history?period=12m"
        val productContent = String(Files.readAllBytes(Path.of("src/test/resources/product.json")))
        Mockito.`when`(restTemplate.getForEntity(urlGetStatistics, String::class.java)).thenReturn(ResponseEntity(productContent, HttpStatusCode.valueOf(200)))

        val toBeInspected = ProductDto(1, productKey, "SomeName", Money(BigDecimal.TEN, BYN))
        val result: PriceStatisticsDto = client.getPriceStatistics(toBeInspected)
        assertEquals(toBeInspected, result.product)
        val expectedFirst = PriceEntryDto(LocalDate.of(2023, 2, 1), Money(BigDecimal.valueOf(999.0), BYN))
        assertEquals(expectedFirst, result.prices[0])
        val expectedSecond = PriceEntryDto(LocalDate.of(2023, 3, 1), Money(BigDecimal.valueOf(937.68), BYN))
        assertEquals(expectedSecond, result.prices[1])
    }

    @Test
    fun getPageable() {
        val urlGetPageable = "$productsUrl$category?$productFilter"
        val productsContent = String(Files.readAllBytes(Path.of("src/test/resources/products.json")))
        Mockito.`when`(restTemplate.getForEntity(urlGetPageable, String::class.java)).thenReturn(ResponseEntity(productsContent, HttpStatusCode.valueOf(200)))

        val result: CategoryPageableDto = client.getPageable(category)
        val expected = CategoryPageableDto(category, 3438, 30, 115)
        assertEquals(expected, result)
    }

}
