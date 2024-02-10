package com.bsuir.hrm.dataanalyzer.data.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType.EAGER
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "products")
class Product(
    val apiKey: String,
    val name: String,
    val category: String,
    @OneToMany(cascade = [CascadeType.ALL], fetch = EAGER)
    @JoinColumn(name = "product_id")
    val prices: List<PriceEntry>,
    @Id
    val id: Long? = null
)
