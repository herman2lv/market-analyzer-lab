package com.bsuir.hrm.dataanalyzer.domain

import java.time.LocalDate

data class PriceEntryDto(
    val date: LocalDate,
    val price: Money,
)
