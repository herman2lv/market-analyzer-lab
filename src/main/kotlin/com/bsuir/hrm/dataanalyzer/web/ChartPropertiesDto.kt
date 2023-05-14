package com.bsuir.hrm.dataanalyzer.web

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

data class ChartPropertiesDto(
    val categories: List<String>,
    @JsonFormat(pattern = "yyyy-MM-dd")
    val start: LocalDate = LocalDate.now().minusYears(1),
    val end: LocalDate = LocalDate.now(),
)
