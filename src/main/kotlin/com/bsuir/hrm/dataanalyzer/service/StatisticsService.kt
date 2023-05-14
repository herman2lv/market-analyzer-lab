package com.bsuir.hrm.dataanalyzer.service

import com.bsuir.hrm.dataanalyzer.domain.Dataset
import com.bsuir.hrm.dataanalyzer.domain.PriceStatisticsDto
import java.math.BigDecimal

interface StatisticsService {

    fun getInflationRateByMonths(prices: List<PriceStatisticsDto>): Dataset

    fun getInflationRateForYear(prices: List<PriceStatisticsDto>): BigDecimal

}
