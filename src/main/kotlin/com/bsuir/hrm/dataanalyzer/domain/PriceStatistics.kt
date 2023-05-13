package com.bsuir.hrm.dataanalyzer.domain

data class PriceStatistics(
    val product: Product,
    val prices: ArrayList<PriceEntry>,
)
