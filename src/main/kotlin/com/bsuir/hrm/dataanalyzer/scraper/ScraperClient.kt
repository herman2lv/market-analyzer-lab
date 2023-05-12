package com.bsuir.hrm.dataanalyzer.scraper

interface ScraperClient {

    fun getProducts(category: String, page: Int = 1): ArrayList<Product>

    fun getProductPriceStatistics(product: Product): PriceStatistics

}
