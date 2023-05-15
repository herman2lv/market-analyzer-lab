package com.bsuir.hrm.dataanalyzer.service.dto

import java.math.BigDecimal
import java.util.Collections

data class DataSeries(
    val label: String,
    private val values: MutableList<BigDecimal> = mutableListOf(),
) {

    fun addValue(value: BigDecimal) {
        this.values.add(value)
    }

    fun getValues(): List<BigDecimal> {
        return Collections.unmodifiableList(values)
    }
}
