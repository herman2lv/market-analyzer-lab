package com.bsuir.hrm.dataanalyzer.domain

import java.math.BigDecimal

data class DataSeries(
    val label: String,
    val values: List<BigDecimal>,
)
