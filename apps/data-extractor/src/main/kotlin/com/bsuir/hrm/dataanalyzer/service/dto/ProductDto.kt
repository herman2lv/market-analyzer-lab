package com.bsuir.hrm.dataanalyzer.service.dto

data class ProductDto(
    val id: Long,
    val key: String,
    val name: String,
    val price: MoneyDto,
)
