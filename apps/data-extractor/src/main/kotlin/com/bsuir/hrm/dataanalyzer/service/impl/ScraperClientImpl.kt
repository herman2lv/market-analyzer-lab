package com.bsuir.hrm.dataanalyzer.service.impl

import com.bsuir.hrm.dataanalyzer.service.ScraperClient
import com.bsuir.hrm.dataanalyzer.service.dto.CategoryPageableDto
import com.bsuir.hrm.dataanalyzer.service.dto.MoneyDto
import com.bsuir.hrm.dataanalyzer.service.dto.MoneyDto.Currency
import com.bsuir.hrm.dataanalyzer.service.dto.PriceEntryDto
import com.bsuir.hrm.dataanalyzer.service.dto.PriceDataDto
import com.bsuir.hrm.dataanalyzer.service.dto.ProductDto
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField

private const val FIRST_DAY_OF_MONTH = 1
private const val PRODUCTS_FILTER = "price[from]=0.00"
private val log: Logger = LoggerFactory.getLogger(ScraperClientImpl::class.java)

@Component
class ScraperClientImpl(
    private val restTemplate: RestTemplate,
    private val jsonMapper: ObjectMapper,
    @Value("\${hrm.api.products.url}")
    private val productsUrl: String,
    @Value("\${hrm.api.prices.url}")
    private val pricesUrl: String,
    @Value("\${hrm.api.prices.date-pattern}")
    dateTimePattern: String,
) : ScraperClient {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern)

    override fun getProducts(category: String, page: Int): List<ProductDto> {
        val url = "$productsUrl$category?page=$page&$PRODUCTS_FILTER"
        val json: JsonNode = getJsonResponse(url)
        return mapProducts(json)
    }

    override fun getPriceStatistics(product: ProductDto): PriceDataDto {
        val url = "$pricesUrl${product.key}/prices-history?period=12m"
        val json = getJsonResponse(url)
        val prices = mapPrices(json)
        return PriceDataDto(product, prices)
    }

    override fun getPageable(category: String): CategoryPageableDto {
        val url = "$productsUrl$category?$PRODUCTS_FILTER"
        val json: JsonNode = getJsonResponse(url)
        return getPageable(json, category)
    }

    private fun getPageable(json: JsonNode, category: String): CategoryPageableDto {
        val totalProducts = json["total"].intValue()
        val pageSize = json["page"]["limit"].intValue()
        val totalPages = json["page"]["last"].intValue()
        return CategoryPageableDto(category, totalProducts, pageSize, totalPages)
    }

    private fun getJsonResponse(url: String): JsonNode {
        log.debug("Sending request to: {}", url)
        val httpEntity: HttpEntity<String> = restTemplate.getForEntity(url, String::class.java)
        val json = jsonMapper.readTree(httpEntity.body)
        log.trace("Received response of size: {} nodes", json.size())
        return json
    }

    private fun mapProducts(json: JsonNode): ArrayList<ProductDto> {
        val products = arrayListOf<ProductDto>()
        json["products"].forEach {
            products.add(mapProduct(it))
        }
        return products
    }

    private fun mapProduct(it: JsonNode): ProductDto {
        val id = it["id"].asLong()
        val key = it["key"].asText()
        val name = it["full_name"].asText()
        val jsonPrice = it["prices"]["price_min"]
        val price = MoneyDto(toDecimal(jsonPrice["amount"]), Currency.valueOf(jsonPrice["currency"].asText()))
        return ProductDto(id, key, name, price)
    }

    private fun mapPrices(json: JsonNode): ArrayList<PriceEntryDto> {
        val prices = arrayListOf<PriceEntryDto>()
        val currency = Currency.valueOf(json["chart_data"]["currency"].asText())
        json["chart_data"]["items"].forEach loop@{
            if (it["price"].isNull) {
                return@loop
            }
            prices.add(mapPriceEntry(it, currency))
        }
        return prices
    }

    private fun mapPriceEntry(it: JsonNode, currency: Currency): PriceEntryDto {
        val price = MoneyDto(toDecimal(it["price"]), currency)
        val parsed = formatter.parse(it["date"].asText())
        val date = LocalDate.of(parsed.get(ChronoField.YEAR), parsed.get(ChronoField.MONTH_OF_YEAR), FIRST_DAY_OF_MONTH)
        return PriceEntryDto(date = date, price = price)
    }

    private fun toDecimal(node: JsonNode) = BigDecimal.valueOf(node.asDouble())
}
