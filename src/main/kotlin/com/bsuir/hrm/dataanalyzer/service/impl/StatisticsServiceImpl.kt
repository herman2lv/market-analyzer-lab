package com.bsuir.hrm.dataanalyzer.service.impl

import com.bsuir.hrm.dataanalyzer.service.dto.DataSeries
import com.bsuir.hrm.dataanalyzer.service.dto.Dataset
import com.bsuir.hrm.dataanalyzer.data.entity.Product
import com.bsuir.hrm.dataanalyzer.service.StatisticsService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

private val log: Logger = LoggerFactory.getLogger(StatisticsServiceImpl::class.java)

@Service
class StatisticsServiceImpl : StatisticsService {

    override fun getInflationRateByMonths(products: List<Product>, start: LocalDate, end: LocalDate): Dataset {
        val labels: MutableList<String> = mutableListOf()
        val seriesRate = DataSeries("Inflation Rate")
        val seriesAverage = DataSeries("AveragePrice")
        val monthly: MutableMap<LocalDate, Sum> = mutableMapOf()
        products.forEach { product ->
            product.prices.forEach { priceEntry ->
                monthly.merge(priceEntry.date.withDayOfMonth(1), Sum(1, priceEntry.price.amount)) { old, new ->
                    Sum(old.count + new.count, old.amount + new.amount)
                }
            }
        }
        var date = start.withDayOfMonth(1)
        while (date.isBefore(end) || date.isEqual(end)) {
            val key = date.withDayOfMonth(1)
            date = date.plusMonths(1)
            val monthSum = monthly[key] ?: continue
            val monthAverage = monthSum.amount / monthSum.count.toBigDecimal()
            labels.add("${date.month}-${date.year}")
            seriesAverage.addValue(monthAverage)
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
