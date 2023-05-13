package com.bsuir.hrm.dataanalyzer.service.impl

import com.bsuir.hrm.dataanalyzer.domain.PriceStatistics
import com.bsuir.hrm.dataanalyzer.domain.Dataset
import com.bsuir.hrm.dataanalyzer.service.StatisticsService
import java.math.BigDecimal

class StatisticsServiceImpl : StatisticsService {

    override fun getInflationRateByMonths(prices: List<PriceStatistics>): Dataset {
        TODO("Not yet implemented")
    }

    override fun getInflationRateForYear(prices: List<PriceStatistics>): BigDecimal {
        TODO("Not yet implemented")
    }
}
