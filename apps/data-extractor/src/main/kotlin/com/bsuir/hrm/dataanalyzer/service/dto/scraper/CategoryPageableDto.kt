package com.bsuir.hrm.dataanalyzer.service.dto.scraper

data class CategoryPageableDto(
    val category: String,
    val totalProducts: Int,
    val pageSize: Int,
    val totalPages: Int,
)
