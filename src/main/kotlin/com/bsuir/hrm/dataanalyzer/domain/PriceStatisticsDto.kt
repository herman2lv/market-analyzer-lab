package com.bsuir.hrm.dataanalyzer.domain

data class PriceStatisticsDto(
    val product: ProductDto,
    val prices: ArrayList<PriceEntry>,
)
