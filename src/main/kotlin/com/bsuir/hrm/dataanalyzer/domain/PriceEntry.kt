package com.bsuir.hrm.dataanalyzer.domain

import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType.DATE
import java.time.LocalDate

@Entity
@Table(name = "price_entries")
data class PriceEntry(
    @Id
    var id: Long? = null,
    @Temporal(DATE)
    val date: LocalDate,
    @Embedded
    val price: Money,
)
