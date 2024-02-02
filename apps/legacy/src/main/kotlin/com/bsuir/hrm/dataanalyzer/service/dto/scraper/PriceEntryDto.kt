package com.bsuir.hrm.dataanalyzer.service.dto.scraper

import com.bsuir.hrm.dataanalyzer.data.entity.Money
import java.time.LocalDate

data class PriceEntryDto(
    val date: LocalDate,
    val price: Money,
)
