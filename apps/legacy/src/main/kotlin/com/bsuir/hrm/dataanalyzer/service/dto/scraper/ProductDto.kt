package com.bsuir.hrm.dataanalyzer.service.dto.scraper

import com.bsuir.hrm.dataanalyzer.data.entity.Money

data class ProductDto(
    val id: Long,
    val key: String,
    val name: String,
    val price: Money,
)
