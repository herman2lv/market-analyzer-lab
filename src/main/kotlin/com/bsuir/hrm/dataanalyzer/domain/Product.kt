package com.bsuir.hrm.dataanalyzer.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "products")
data class Product(
    @Id
    var id: Long,
    var apiKey: String,
    var name: String,
    var category: String,
    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn(name = "product_id")
    var prices: List<PriceEntry>,
)
