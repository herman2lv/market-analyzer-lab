package com.bsuir.hrm.dataanalyzer.service

import com.bsuir.hrm.dataanalyzer.service.dto.CategoryPageableDto
import com.bsuir.hrm.dataanalyzer.service.dto.PriceDataDto
import com.bsuir.hrm.dataanalyzer.service.dto.ProductDto

interface ScraperClient {

    fun getProducts(category: String, page: Int = 1): List<ProductDto>

    fun getPriceStatistics(product: ProductDto): PriceDataDto

    fun getPageable(category: String): CategoryPageableDto
}
