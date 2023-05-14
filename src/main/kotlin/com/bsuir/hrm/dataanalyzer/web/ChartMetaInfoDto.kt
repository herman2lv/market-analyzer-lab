package com.bsuir.hrm.dataanalyzer.web

import java.math.BigDecimal

data class ChartMetaInfoDto(
    val title: String,
    val total: BigDecimal,
    val description: String,
)
