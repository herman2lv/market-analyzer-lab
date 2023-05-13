package com.bsuir.hrm.dataanalyzer.scraper

interface ScraperClient {

    fun getProducts(category: String, page: Int = 1): ArrayList<Product>

    fun getPriceStatistics(product: Product): PriceStatistics

    fun getPageable(category: String): CategoryPageable
}
