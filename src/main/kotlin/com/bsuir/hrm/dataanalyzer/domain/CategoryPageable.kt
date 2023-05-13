package com.bsuir.hrm.dataanalyzer.domain

data class CategoryPageable(
    val category: String,
    val totalProducts: Int,
    val pageSize: Int,
    val totalPages: Int,
)
