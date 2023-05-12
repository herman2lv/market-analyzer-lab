package com.bsuir.hrm.dataanalyzer.scraper

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal

@Component
class ScraperClientImpl(
    val restTemplate: RestTemplate,
    val jsonMapper: ObjectMapper,
) : ScraperClient {


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
            val price = Money(BigDecimal.valueOf(jsonPrice["amount"].asDouble()), Currency.valueOf(jsonPrice["currency"].asText()))
            products.add(Product(id, key, name, price))
        }
        return products
    }

    override fun getProductPriceStatistics(product: Product): PriceStatistics {
        TODO("Not yet implemented")
    }
}
