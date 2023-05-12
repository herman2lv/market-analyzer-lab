package com.bsuir.hrm.dataanalyzer.scraper

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField

@Component
class ScraperClientImpl(
    val restTemplate: RestTemplate,
    val jsonMapper: ObjectMapper,
) : ScraperClient {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM")

    override fun getProducts(category: String, page: Int): ArrayList<Product> {
        val url = "https://catalog.onliner.by/sdapi/catalog.api/search/${category}?page=${page}&order=price:asc"
        val httpEntity: HttpEntity<String> = restTemplate.getForEntity(url, String::class.java)
        val json: JsonNode = jsonMapper.readTree(httpEntity.body)
        val products = arrayListOf<Product>()
        json["products"].onEach {
            val id = it["id"].asLong()
            val key = it["key"].asText()
            val name = it["full_name"].asText()
            val jsonPrice = it["prices"]["price_min"]
            val price = Money(toDecimal(jsonPrice["amount"]), Currency.valueOf(jsonPrice["currency"].asText()))
            products.add(Product(id, key, name, price))
        }
        return products
    }

    override fun getPriceStatistics(product: Product): PriceStatistics {
        val url = "https://catalog.api.onliner.by/products/${product.key}/prices-history?period=12m"
        val httpEntity = restTemplate.getForEntity(url, String::class.java)
        val json: JsonNode = jsonMapper.readTree(httpEntity.body)
        val currency = Currency.valueOf(json["chart_data"]["currency"].asText())
        val prices = arrayListOf<PriceEntry>()
        json["chart_data"]["items"].onEach loop@{
            if (it["price"].isNull) {
                return@loop
            }
            val price = Money(toDecimal(it["price"]), currency)
            val parsed = formatter.parse(it["date"].asText())
            val date = LocalDate.of(parsed.get(ChronoField.YEAR), parsed.get(ChronoField.MONTH_OF_YEAR), 1)
            prices.add(PriceEntry(date, price))
        }
        return PriceStatistics(product, prices)
    }

    private fun toDecimal(node: JsonNode) = BigDecimal.valueOf(node.asDouble())
}
