package com.bsuir.hrm.dataanalyzer.web

import java.time.LocalDate
import java.time.Month

data class ChartPropertiesDto(
    val categories: List<String>,
    val begin: Month = LocalDate.now().minusYears(1).month,
    val end: Month = LocalDate.now().month,
)
