package com.bsuir.hrm.dataanalyzer.service.dto

class Product(
    val apiKey: String,
    val name: String,
    val category: String,
    val prices: List<PriceEntry>,
    val id: Long? = null
)
