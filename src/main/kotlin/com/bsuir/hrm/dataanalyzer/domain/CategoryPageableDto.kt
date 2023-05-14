package com.bsuir.hrm.dataanalyzer.domain

data class CategoryPageableDto(
    val category: String,
    val totalProducts: Int,
    val pageSize: Int,
    val totalPages: Int,
)
