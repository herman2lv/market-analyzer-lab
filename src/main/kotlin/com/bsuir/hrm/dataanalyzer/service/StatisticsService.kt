package com.bsuir.hrm.dataanalyzer.service

import com.bsuir.hrm.dataanalyzer.service.dto.Dataset
import com.bsuir.hrm.dataanalyzer.data.entity.Product
import java.math.BigDecimal
import java.time.LocalDate

interface StatisticsService {

    fun getInflationRateByMonths(products: List<Product>, start: LocalDate, end: LocalDate): Dataset

    fun getInflationRateForYear(products: List<Product>): BigDecimal

}
