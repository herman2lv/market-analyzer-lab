package com.bsuir.hrm.dataanalyzer.service.impl

import com.bsuir.hrm.dataanalyzer.client.ProductServiceClient
import com.bsuir.hrm.dataanalyzer.service.StatisticsService
import com.bsuir.hrm.dataanalyzer.service.dto.DataSeries
import com.bsuir.hrm.dataanalyzer.service.dto.Dataset
import com.bsuir.hrm.dataanalyzer.service.dto.ProcessingProperties
import com.bsuir.hrm.dataanalyzer.service.dto.Product
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

private val log: Logger = LoggerFactory.getLogger(StatisticsServiceImpl::class.java)

@Service
class StatisticsServiceImpl(
    private val productServiceClient: ProductServiceClient
) : StatisticsService {

    override fun getDataset(properties: ProcessingProperties): Map<String, Dataset> {
        log.debug("Received properties: {}", properties)
        val products = productServiceClient.getProducts(properties.categories)
        log.debug("Got products list of size: {}", products.size)
        return getInflationRateByMonths(products, properties)
    }

    private fun getInflationRateByMonths(products: List<Product>, properties: ProcessingProperties): Map<String, Dataset> {
        val sumByMonth: MutableMap<LocalDate, Sum> = groupSumByDates(products)
        val labels: MutableList<String> = mutableListOf()
        val seriesAverage = getSeriesAverage(properties, sumByMonth, labels)
        val seriesInflation = getSeriesInflation(seriesAverage)

        val datasetInflation = Dataset(labels)
        datasetInflation.addDataSeries(seriesInflation)

        val datasetAverage = Dataset(labels)
        if (!properties.group) {
            val mapByCategory: MutableMap<String, MutableList<Product>> = mapByCategory(products)
            mapByCategory.forEach { entry ->
                val sumByMonthCategory: MutableMap<LocalDate, Sum> = groupSumByDates(entry.value)
                val seriesAverageCategory = getSeriesAverage(properties, sumByMonthCategory)
                datasetAverage.addDataSeries(seriesAverageCategory)
            }
        } else {
            datasetAverage.addDataSeries(seriesAverage)
        }

        log.debug("Datasets to be returned: {}, {}", datasetAverage, datasetInflation)
        return mapOf(Pair("inflation", datasetInflation), Pair("average", datasetAverage))
    }

    private fun mapByCategory(products: List<Product>): MutableMap<String, MutableList<Product>> {
        val mapByCategory: MutableMap<String, MutableList<Product>> = mutableMapOf()
        products.forEach { product ->
            mapByCategory.merge(product.category, mutableListOf(product)) { old, new ->
                old.addAll(new)
                old
            }
        }
        return mapByCategory
    }

    private fun getSeriesInflation(seriesAverage: DataSeries): DataSeries {
        val seriesInflation = DataSeries("Inflation Rate")
        var prevAverage = seriesAverage.getValues()[0]
        for (average in seriesAverage.getValues()) {
            val inflationRate = (average - prevAverage) / prevAverage
            seriesInflation.addValue(inflationRate)
            prevAverage = average
        }
        return seriesInflation
    }

    private fun getSeriesAverage(
        properties: ProcessingProperties,
        sumByMonth: MutableMap<LocalDate, Sum>,
        labels: MutableList<String>? = null
    ): DataSeries {
        val seriesAverage = DataSeries("AveragePrice")
        var date = properties.start.withDayOfMonth(1)
        while (date.isBefore(properties.end) || date.isEqual(properties.end)) {
            val key = date.withDayOfMonth(1)
            date = date.plusMonths(1)
            val monthSum = sumByMonth[key] ?: continue
            val monthAverage = monthSum.amount / monthSum.count.toBigDecimal()
            labels?.add("${key.month.toString().substring(0..2)}-${key.year}")
            seriesAverage.addValue(monthAverage)
        }
        return seriesAverage
    }

    private fun groupSumByDates(products: List<Product>): MutableMap<LocalDate, Sum> {
        val monthly: MutableMap<LocalDate, Sum> = mutableMapOf()
        products.forEach { product ->
            product.prices.forEach { priceEntry ->
                monthly.merge(priceEntry.date.withDayOfMonth(1), Sum(1, priceEntry.price.amount)) { old, new ->
                    Sum(old.count + new.count, old.amount + new.amount)
                }
            }
        }
        return monthly
    }

    private data class Sum(val count: Int, val amount: BigDecimal)
}
