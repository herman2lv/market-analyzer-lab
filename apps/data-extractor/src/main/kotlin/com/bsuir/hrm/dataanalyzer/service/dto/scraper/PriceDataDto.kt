package com.bsuir.hrm.dataanalyzer.service.dto.scraper

data class PriceDataDto(
    val product: ProductDto,
    val prices: List<PriceEntryDto>,
)
