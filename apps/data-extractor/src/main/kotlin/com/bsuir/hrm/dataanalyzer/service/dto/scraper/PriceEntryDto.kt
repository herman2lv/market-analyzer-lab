package com.bsuir.hrm.dataanalyzer.service.dto.scraper

import java.time.LocalDate

data class PriceEntryDto(
    val date: LocalDate,
    val price: MoneyDto,
)
