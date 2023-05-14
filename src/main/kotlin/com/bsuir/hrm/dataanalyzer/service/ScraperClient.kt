package com.bsuir.hrm.dataanalyzer.service

import com.bsuir.hrm.dataanalyzer.domain.CategoryPageableDto
import com.bsuir.hrm.dataanalyzer.domain.PriceStatisticsDto
import com.bsuir.hrm.dataanalyzer.domain.ProductDto

interface ScraperClient {

    fun getProducts(category: String, page: Int = 1): ArrayList<ProductDto>

    fun getPriceStatistics(product: ProductDto): PriceStatisticsDto

    fun getPageable(category: String): CategoryPageableDto
}
