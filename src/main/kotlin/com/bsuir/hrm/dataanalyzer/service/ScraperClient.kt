package com.bsuir.hrm.dataanalyzer.service

import com.bsuir.hrm.dataanalyzer.service.dto.scraper.CategoryPageableDto
import com.bsuir.hrm.dataanalyzer.service.dto.scraper.PriceStatisticsDto
import com.bsuir.hrm.dataanalyzer.service.dto.scraper.ProductDto

interface ScraperClient {

    fun getProducts(category: String, page: Int = 1): ArrayList<ProductDto>

    fun getPriceStatistics(product: ProductDto): PriceStatisticsDto

    fun getPageable(category: String): CategoryPageableDto
}
