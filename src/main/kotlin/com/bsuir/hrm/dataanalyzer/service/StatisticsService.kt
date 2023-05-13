package com.bsuir.hrm.dataanalyzer.service

import com.bsuir.hrm.dataanalyzer.domain.Dataset
import com.bsuir.hrm.dataanalyzer.domain.PriceStatistics
import java.math.BigDecimal

interface StatisticsService {

    fun getInflationRateByMonths(prices: List<PriceStatistics>): Dataset

    fun getInflationRateForYear(prices: List<PriceStatistics>): BigDecimal

}
