package com.bsuir.hrm.dataanalyzer.scraper

interface ScraperClient {

    fun getProducts(category: String, page: Int = 1): Array<Product>

    fun getProductPriceStatistics(product: Product): PriceStatistics

}
