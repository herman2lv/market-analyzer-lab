package com.bsuir.hrm.dataanalyzer.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "products")
data class Product(
    @Id
    val id: Long,
    val key: String,
    val name: String,
    @OneToMany
    @JoinColumn(name  = "product_id")
    val prices: ArrayList<PriceEntry>,
)
