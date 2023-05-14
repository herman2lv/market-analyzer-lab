package com.bsuir.hrm.dataanalyzer.service.impl

import com.bsuir.hrm.dataanalyzer.domain.DataSeries
import com.bsuir.hrm.dataanalyzer.domain.Dataset
import com.bsuir.hrm.dataanalyzer.domain.Product
import com.bsuir.hrm.dataanalyzer.service.StatisticsService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month

private val log: Logger = LoggerFactory.getLogger(StatisticsServiceImpl::class.java)

@Service
class StatisticsServiceImpl : StatisticsService {

    override fun getInflationRateByMonths(products: List<Product>): Dataset {
        val labels: MutableList<String> = mutableListOf()
        val seriesRate = DataSeries("Inflation Rate")
        val seriesAverage = DataSeries("AveragePrice")
        val monthly: MutableMap<Month, Sum> = mutableMapOf()
        products.forEach { product ->
            product.prices.forEach { priceEntry ->
                monthly.merge(priceEntry.date.month, Sum(1, priceEntry.price.amount)) { old, new ->
                    Sum(old.count + new.count, old.amount + new.amount)
                }
            }
        }
        val endDate = LocalDate.now()
        var date = endDate.minusYears(1)
        while (date.isBefore(endDate)) {
            val monthSum = monthly[date.month] ?: continue
            val monthAverage = monthSum.amount / monthSum.count.toBigDecimal()
            labels.add(date.month.toString())
            seriesAverage.addValue(monthAverage)
            date = date.plusMonths(1)
        }

        var prevAverage = seriesAverage.getValues()[0]
        for (average in seriesAverage.getValues()) {
            val inflationRate = (average - prevAverage) / prevAverage
            seriesRate.addValue(inflationRate)
            prevAverage = average
        }
        val dataset = Dataset(labels)
        dataset.addDataSeries(seriesRate, seriesAverage)
        log.debug("Dataset to be returned: {}", dataset)
        return dataset
    }

    override fun getInflationRateForYear(products: List<Product>): BigDecimal {
        TODO("Not yet implemented")
    }

    private data class Sum(val count: Int, val amount: BigDecimal)
}
