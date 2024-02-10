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
class PriceEntry(
    @Temporal(DATE)
    val date: LocalDate,
    @Embedded
    val price: Money,
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long? = null
)
