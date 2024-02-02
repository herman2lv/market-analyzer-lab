package com.bsuir.hrm.dataanalyzer.service.dto

import java.time.LocalDate

data class ChartPropertiesDto(
    val categories: List<String>,
    val start: LocalDate = LocalDate.now().minusYears(1),
    val end: LocalDate = LocalDate.now(),
    val group: Boolean = true,
)
