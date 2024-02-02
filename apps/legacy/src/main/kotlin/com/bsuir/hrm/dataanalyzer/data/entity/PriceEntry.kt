package com.bsuir.hrm.dataanalyzer.data.entity

import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType.DATE
import java.time.LocalDate

@Entity
@Table(name = "price_entries", indexes = [Index(columnList = "product_id")])
data class PriceEntry(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    var id: Long? = null,
    @Temporal(DATE)
    var date: LocalDate,
    @Embedded
    var price: Money,
)
