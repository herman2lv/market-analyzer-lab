package com.bsuir.hrm.dataanalyzer.domain

import java.time.LocalDate

data class PriceEntry(
    val date: LocalDate,
    val price: Money,
)
