package com.bsuir.hrm.dataanalyzer.service.impl

import com.bsuir.hrm.dataanalyzer.client.DataExtractorClient
import com.bsuir.hrm.dataanalyzer.data.CacheMetaDataRepository
import com.bsuir.hrm.dataanalyzer.data.ProductRepository
import com.bsuir.hrm.dataanalyzer.data.entity.CacheMetaData
import com.bsuir.hrm.dataanalyzer.service.dto.CategoryMetaInfo
import com.bsuir.hrm.dataanalyzer.data.entity.Money
import com.bsuir.hrm.dataanalyzer.data.entity.Money.Currency
import com.bsuir.hrm.dataanalyzer.data.entity.PriceEntry
import com.bsuir.hrm.dataanalyzer.data.entity.Product
import com.bsuir.hrm.dataanalyzer.service.ProductService
import com.bsuir.hrm.dataanalyzer.service.dto.PriceDataDto
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

private val log: Logger = LoggerFactory.getLogger(ProductServiceImpl::class.java)
private const val TRANSACTION_SIZE = 10
private const val CACHE_LIVE_DAYS: Long = 30

@Service
class ProductServiceImpl(
    private val dataExtractorClient: DataExtractorClient,
    private val productRepository: ProductRepository,
    private val cacheMetaDataRepository: CacheMetaDataRepository,
    private val jsonMapper: ObjectMapper,
) : ProductService {

    override fun getPriceData(category: String): List<PriceDataDto> {
        log.debug("Get price data for category={}", category)
        val step = 5
        val limit = 20
        var total = 0
        val prices: MutableList<PriceDataDto> = mutableListOf()
        while (total < limit) {
            log.debug("calling client with offset={}, limit={}", total, step)
            val priceStatistics = dataExtractorClient.getPriceStatistics(category, total, step)
            prices.addAll(priceStatistics)
            total += priceStatistics.size
        }
        return prices
    }

    private lateinit var categories: List<CategoryMetaInfo>

    override fun getProductsInCategories(categories: List<String>): List<Product> {
        log.debug("Received categories: {}", categories)
        categories.forEach { category ->
            cacheMetaDataRepository.findById(category)
                .ifPresentOrElse({
                    log.debug("Cache metadata: {}", it.category)
                    if (isOld(it)) {
                        update(category)
                    }
                }, { update(category) })
        }
        return productRepository.findAllByCategoryIn(categories)
    }

    override fun getCategoriesMetaInfo(): List<CategoryMetaInfo> {
        if (!this::categories.isInitialized) {
            categories = getAllInternal()
        }
        return categories
    }

    private fun getAllInternal(): List<CategoryMetaInfo> {
        val reader = javaClass.getResourceAsStream("/categories.json") ?: throw RuntimeException("Configuration error")
        reader.use {
            val json: JsonNode = jsonMapper.readTree(reader.readAllBytes())
            log.info("Read categories list")
            log.debug("Categories: size={}", json.size())
            return json.map { CategoryMetaInfo(it["key"].asText(), it["display"].asText()) }
        }
    }

    private fun isOld(cache: CacheMetaData) = cache.uploadTime.isBefore(LocalDateTime.now().minusDays(CACHE_LIVE_DAYS))

    private fun update(category: String) {
        log.debug("Cache metadata for category={} is to be updated", category)
        val uploadTime = LocalDateTime.now()
        val pricesInfos = getPriceData(category)

        val products: MutableList<Product> = mutableListOf()
        pricesInfos.forEach { pricesInfo ->
            val product = Product(
                pricesInfo.product.key,
                pricesInfo.product.name,
                category,
                pricesInfo.prices.map { PriceEntry(date = it.date, price = Money(it.price.amount, Currency.valueOf(it.price.currency.name))) },
                pricesInfo.product.id
            )
            products.add(product)
            if (products.size == TRANSACTION_SIZE) {
                productRepository.saveAll(products)
                products.clear()
            }
        }

        val updated = cacheMetaDataRepository.save(CacheMetaData(category, uploadTime))
        log.debug("Updated cache: {}", updated)
    }

}
