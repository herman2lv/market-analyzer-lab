package com.bsuir.hrm.dataanalyzer.domain

data class Product(
    val id: Long,
    val key: String,
    val name: String,
    val price: Money,
)
