package com.bsuir.hrm.dataanalyzer.domain

data class ProductDto(
    val id: Long,
    val key: String,
    val name: String,
    val price: Money,
)
