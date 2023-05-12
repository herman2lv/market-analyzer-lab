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

private const val FIRST_DAY_OF_MONTH = 1

@Component
class ScraperClientImpl(
    val restTemplate: RestTemplate,
    val jsonMapper: ObjectMapper,
) : ScraperClient {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM")//FIXME externalize props

    override fun getProducts(category: String, page: Int): ArrayList<Product> {
        val url = "https://catalog.onliner.by/sdapi/catalog.api/search/${category}?page=${page}&order=price:asc" //FIXME filter without price but remove price sorting
        val json: JsonNode = getJsonResponse(url)
        return mapProducts(json)
    }

    override fun getPriceStatistics(product: Product): PriceStatistics {
        val url = "https://catalog.api.onliner.by/products/${product.key}/prices-history?period=12m"
        val json = getJsonResponse(url)
        val prices = mapPrices(json)
        return PriceStatistics(product, prices)
    }

    private fun getJsonResponse(url: String): JsonNode {
        val httpEntity: HttpEntity<String> = restTemplate.getForEntity(url, String::class.java)
        return jsonMapper.readTree(httpEntity.body)
    }

    private fun mapProducts(json: JsonNode): ArrayList<Product> {
        val products = arrayListOf<Product>()
        json["products"].onEach {
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
        json["chart_data"]["items"].onEach loop@{
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
