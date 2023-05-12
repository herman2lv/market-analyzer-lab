package com.bsuir.hrm.dataanalyzer.scraper

data class PriceStatistics(
    val product: Product,
    val prices: Array<PriceEntry>,
)
