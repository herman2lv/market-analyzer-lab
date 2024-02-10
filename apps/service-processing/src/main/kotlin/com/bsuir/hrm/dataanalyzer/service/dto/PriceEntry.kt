package com.bsuir.hrm.dataanalyzer.service.dto

import java.time.LocalDate

class PriceEntry(
    val date: LocalDate,
    val price: Money,
    val id: Long? = null
)
