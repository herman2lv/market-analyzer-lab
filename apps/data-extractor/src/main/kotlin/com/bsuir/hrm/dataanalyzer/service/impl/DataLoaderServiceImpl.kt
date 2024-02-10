package com.bsuir.hrm.dataanalyzer.service.impl

import com.bsuir.hrm.dataanalyzer.service.DataLoaderService
import com.bsuir.hrm.dataanalyzer.service.ScraperClient
import com.bsuir.hrm.dataanalyzer.service.dto.PriceDataDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

private val log: Logger = LoggerFactory.getLogger(DataLoaderServiceImpl::class.java)

@Service
class DataLoaderServiceImpl(
    private val scraperClient: ScraperClient
) : DataLoaderService {

    override fun getPriceStatistics(category: String, offset: Int, limit: Int): List<PriceDataDto> {
        log.debug("Cache metadata is to be updated")
        val pages = scraperClient.getPageable(category)
        val offsetPages = offset / pages.pageSize
        var pageOffset = offset % pages.pageSize

        log.debug("To be loaded: category={}, items={}, pages={}", category, pages.totalProducts, pages.totalPages)
        val priceDataList = mutableListOf<PriceDataDto>()
        var total = 0
        for (page in offsetPages + 1..pages.totalPages) {
            log.debug("Page {} is processing...", page)
            var products = scraperClient.getProducts(category, page)
            if (pageOffset != 0) {
                products = products.subList(pageOffset, products.size - 1)
                pageOffset = 0
            }
            run processPage@{
                products.forEach { product ->
                    if (total == limit) {
                        return@processPage
                    }
                    log.trace("Details for item={} is loading...", product)
                    val pricesInfo = scraperClient.getPriceStatistics(product)
                    log.trace("For item {} is loaded {} records", product, pricesInfo.prices.size)
                    priceDataList.add(pricesInfo)
                    total++
                }
            }
            if (total == limit) {
                return priceDataList
            }
        }
        return priceDataList
    }
}
