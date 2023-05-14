package com.bsuir.hrm.dataanalyzer.service.impl

import com.bsuir.hrm.dataanalyzer.data.CacheMetaDataRepository
import com.bsuir.hrm.dataanalyzer.data.ProductRepository
import com.bsuir.hrm.dataanalyzer.domain.CacheMetaData
import com.bsuir.hrm.dataanalyzer.domain.PriceEntry
import com.bsuir.hrm.dataanalyzer.domain.PriceStatisticsDto
import com.bsuir.hrm.dataanalyzer.domain.Product
import com.bsuir.hrm.dataanalyzer.service.ProductService
import com.bsuir.hrm.dataanalyzer.service.ScraperClient
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ProductServiceImpl(
    val productRepository: ProductRepository,
    val cacheMetaDataRepository: CacheMetaDataRepository,
    val scraperClient: ScraperClient,
) : ProductService {

    override fun findAllByCategoryIn(categories: List<String>): List<Product> {
        categories.forEach { category ->
            cacheMetaDataRepository.findById(category)
                .ifPresentOrElse({ if (isOld(it)) update(category) }, { update(category) })
        }
        return productRepository.findAllByCategoryIn(categories)
    }

    private fun isOld(cache: CacheMetaData) = cache.uploadTime.isBefore(LocalDateTime.now().minusDays(10))

    private fun update(category: String) {
        val uploadTime = LocalDateTime.now()
        val pages = scraperClient.getPageable(category)
        val pricesInfos = mutableListOf<PriceStatisticsDto>()
        for (page in 1..1) {//FIXME remove after initial testing
//        for (page in 1..pages.totalPages) {
            val products = scraperClient.getProducts(category, page)
            products.forEach { product ->
                val pricesInfo = scraperClient.getPriceStatistics(product)
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
        }

        cacheMetaDataRepository.save(CacheMetaData(category, uploadTime))
    }

}
