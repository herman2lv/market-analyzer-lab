package com.bsuir.hrm.dataanalyzer.scraper

data class CategoryPageable(
    val category: String,
    val totalProducts: Int,
    val pageSize: Int,
    val totalPages: Int,
)
