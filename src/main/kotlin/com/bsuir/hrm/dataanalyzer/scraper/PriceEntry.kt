package com.bsuir.hrm.dataanalyzer.scraper

import java.time.LocalDate

data class PriceEntry(
    val date: LocalDate,
    val price: Money,
)
