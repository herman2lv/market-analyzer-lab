package com.bsuir.hrm.dataanalyzer.service.dto.scraper

data class PriceStatisticsDto(
    val product: ProductDto,
    val prices: ArrayList<PriceEntryDto>,
)
