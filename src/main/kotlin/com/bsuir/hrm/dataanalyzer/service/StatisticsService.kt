package com.bsuir.hrm.dataanalyzer.service

import com.bsuir.hrm.dataanalyzer.domain.Dataset
import com.bsuir.hrm.dataanalyzer.domain.Product
import java.math.BigDecimal

interface StatisticsService {

    fun getInflationRateByMonths(products: List<Product>): Dataset

    fun getInflationRateForYear(products: List<Product>): BigDecimal

}
