package com.bsuir.hrm.dataanalyzer.service.impl

import com.bsuir.hrm.dataanalyzer.data.CacheMetaDataRepository
import com.bsuir.hrm.dataanalyzer.data.ProductRepository
import com.bsuir.hrm.dataanalyzer.domain.CacheMetaData
import com.bsuir.hrm.dataanalyzer.domain.PriceEntry
import com.bsuir.hrm.dataanalyzer.domain.PriceStatisticsDto
import com.bsuir.hrm.dataanalyzer.domain.Product
import com.bsuir.hrm.dataanalyzer.service.ProductService
import com.bsuir.hrm.dataanalyzer.service.ScraperClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

private val log: Logger = LoggerFactory.getLogger(ProductServiceImpl::class.java)

@Service
class ProductServiceImpl(
    val productRepository: ProductRepository,
    val cacheMetaDataRepository: CacheMetaDataRepository,
    val scraperClient: ScraperClient,
) : ProductService {

    override fun findAllByCategoryIn(categories: List<String>): List<Product> {
        log.debug("Received categories: {}", categories)
        categories.forEach { category ->
            cacheMetaDataRepository.findById(category)
                .ifPresentOrElse({
                    log.debug("Cache metadata: {}", it)
                    if (isOld(it)) {
                        update(category)
                    }
                }, { update(category) })
        }
        return productRepository.findAllByCategoryIn(categories)
    }

    private fun isOld(cache: CacheMetaData) = cache.uploadTime.isBefore(LocalDateTime.now().minusDays(10))

    private fun update(category: String) {
        log.debug("Cache metadata is to be updated")
        val uploadTime = LocalDateTime.now()
        val pages = scraperClient.getPageable(category)
        log.debug("To be loaded: category={}, items={}, pages={}", category, pages.totalProducts, pages.totalPages)
        val pricesInfos = mutableListOf<PriceStatisticsDto>()
        for (page in 1..1) {//FIXME remove after initial testing
//        for (page in 1..pages.totalPages) {
            log.debug("Page {} is processing...", page)
            val products = scraperClient.getProducts(category, page)
            products.forEach { product ->
                log.trace("Details for item={} is loading...", product)
                val pricesInfo = scraperClient.getPriceStatistics(product)
                log.trace("For item {} is loaded {} records", product, pricesInfo.prices.size)
                pricesInfos.add(pricesInfo)
            }
        }

        pricesInfos.forEach { pricesInfo ->
            val product = Product(
                pricesInfo.product.id,
                pricesInfo.product.key,
                pricesInfo.product.name,
                category,
                pricesInfo.prices.map { PriceEntry(date = it.date, price = it.price) }
            )
            productRepository.save(product)
            log.debug("Saved product: {}", product)
        }

        val updated = cacheMetaDataRepository.save(CacheMetaData(category, uploadTime))
        log.debug("Updated cache: {}", updated)
    }

}
