package com.bsuir.hrm.dataanalyzer.service

import com.bsuir.hrm.dataanalyzer.domain.CategoryPageable
import com.bsuir.hrm.dataanalyzer.domain.PriceStatistics
import com.bsuir.hrm.dataanalyzer.domain.Product

interface ScraperClient {

    fun getProducts(category: String, page: Int = 1): ArrayList<Product>

    fun getPriceStatistics(product: Product): PriceStatistics

    fun getPageable(category: String): CategoryPageable
}
