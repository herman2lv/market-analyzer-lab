package com.bsuir.hrm.dataanalyzer.service.impl

import com.bsuir.hrm.dataanalyzer.service.DataLoaderService
import com.bsuir.hrm.dataanalyzer.service.ScraperClient
import com.bsuir.hrm.dataanalyzer.service.dto.scraper.PriceDataDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

private val log: Logger = LoggerFactory.getLogger(DataLoaderServiceImpl::class.java)

@Service
class DataLoaderServiceImpl(
    private val scraperClient: ScraperClient
) : DataLoaderService {

    override fun getPriceStatistics(category: String): List<PriceDataDto> {
        log.debug("Cache metadata is to be updated")
        val pages = scraperClient.getPageable(category)
        log.debug("To be loaded: category={}, items={}, pages={}", category, pages.totalProducts, pages.totalPages)
        val priceDataList = mutableListOf<PriceDataDto>()
        for (page in 1..pages.totalPages) {
            log.debug("Page {} is processing...", page)
            val products = scraperClient.getProducts(category, page)
            products.forEach { product ->
                log.trace("Details for item={} is loading...", product)
                val pricesInfo = scraperClient.getPriceStatistics(product)
                log.trace("For item {} is loaded {} records", product, pricesInfo.prices.size)
                priceDataList.add(pricesInfo)
            }
        }
        return priceDataList
    }
}
