package com.bsuir.hrm.dataanalyzer.service.impl

import com.bsuir.hrm.dataanalyzer.domain.CategoryPageable
import com.bsuir.hrm.dataanalyzer.domain.Currency
import com.bsuir.hrm.dataanalyzer.domain.Money
import com.bsuir.hrm.dataanalyzer.domain.PriceEntry
import com.bsuir.hrm.dataanalyzer.domain.PriceStatistics
import com.bsuir.hrm.dataanalyzer.domain.Product
import com.bsuir.hrm.dataanalyzer.service.ScraperClient
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField

private const val FIRST_DAY_OF_MONTH = 1
private const val PRODUCTS_FILTER = "order=price:asc"

@Component
class ScraperClientImpl(
    val restTemplate: RestTemplate,
    val jsonMapper: ObjectMapper,
    @Value("\${hrm.api.prices.date-pattern}") dateTimePattern: String,
    @Value("\${hrm.api.products.url}") val productsUrl: String,
    @Value("\${hrm.api.prices.url}") val pricesUrl: String,
) : ScraperClient {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern)

    override fun getProducts(category: String, page: Int): ArrayList<Product> {
        val url = "$productsUrl$category?page=$page&$PRODUCTS_FILTER"
        val json: JsonNode = getJsonResponse(url)
        return mapProducts(json)
    }

    override fun getPriceStatistics(product: Product): PriceStatistics {
        val url = "$pricesUrl${product.key}/prices-history?period=12m"
        val json = getJsonResponse(url)
        val prices = mapPrices(json)
        return PriceStatistics(product, prices)
    }

    override fun getPageable(category: String): CategoryPageable {
        val url = "$productsUrl$category?$PRODUCTS_FILTER"
        val json: JsonNode = getJsonResponse(url)
        return getPageable(json, category)
    }

    private fun getPageable(json: JsonNode, category: String): CategoryPageable {
        val totalProducts = json["total"].intValue()
        val pageSize = json["page"]["limit"].intValue()
        val totalPages = json["page"]["last"].intValue()
        return CategoryPageable(category, totalProducts, pageSize, totalPages)
    }

    private fun getJsonResponse(url: String): JsonNode {
        val httpEntity: HttpEntity<String> = restTemplate.getForEntity(url, String::class.java)
        return jsonMapper.readTree(httpEntity.body)
    }

    private fun mapProducts(json: JsonNode): ArrayList<Product> {
        val products = arrayListOf<Product>()
        json["products"].forEach {
            products.add(mapProduct(it))
        }
        return products
    }

    private fun mapProduct(it: JsonNode): Product {
        val id = it["id"].asLong()
        val key = it["key"].asText()
        val name = it["full_name"].asText()
        val jsonPrice = it["prices"]["price_min"]
        val price = Money(toDecimal(jsonPrice["amount"]), Currency.valueOf(jsonPrice["currency"].asText()))
        return Product(id, key, name, price)
    }

    private fun mapPrices(json: JsonNode): ArrayList<PriceEntry> {
        val prices = arrayListOf<PriceEntry>()
        val currency = Currency.valueOf(json["chart_data"]["currency"].asText())
        json["chart_data"]["items"].forEach loop@{
            if (it["price"].isNull) {
                return@loop
            }
            prices.add(mapPriceEntry(it, currency))
        }
        return prices
    }

    private fun mapPriceEntry(it: JsonNode, currency: Currency): PriceEntry {
        val price = Money(toDecimal(it["price"]), currency)
        val parsed = formatter.parse(it["date"].asText())
        val date = LocalDate.of(parsed.get(ChronoField.YEAR), parsed.get(ChronoField.MONTH_OF_YEAR), FIRST_DAY_OF_MONTH)
        return PriceEntry(date, price)
    }

    private fun toDecimal(node: JsonNode) = BigDecimal.valueOf(node.asDouble())
}
