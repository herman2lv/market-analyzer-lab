package com.bsuir.hrm.dataanalyzer.service.dto

data class PriceDataDto(
    val product: ProductDto,
    val prices: ArrayList<PriceEntryDto>,
)
