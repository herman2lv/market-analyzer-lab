package com.bsuir.hrm.dataanalyzer.service.impl

import com.bsuir.hrm.dataanalyzer.data.entity.Money
import com.bsuir.hrm.dataanalyzer.data.entity.Money.Currency.BYN
import com.bsuir.hrm.dataanalyzer.data.entity.PriceEntry
import com.bsuir.hrm.dataanalyzer.data.entity.Product
import com.bsuir.hrm.dataanalyzer.service.StatisticsService
import com.bsuir.hrm.dataanalyzer.service.dto.ChartPropertiesDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

private val products: MutableList<Product> = mutableListOf()
private const val PRODUCTS = 100
private const val CATEGORIES = 2

private const val INFLATION_KEY = "inflation"

private const val AVERAGE_KEY = "average"

internal class StatisticsServiceImplTest {
    private val statisticsService: StatisticsService = StatisticsServiceImpl()

    @Test
    fun validResponseStructureInflation() {
        val inflationRateByMonths = statisticsService.getInflationRateByMonths(products, ChartPropertiesDto(listOf()))
        assertTrue(inflationRateByMonths.containsKey(INFLATION_KEY))
    }

    @Test
    fun validResponseStructureAverage() {
        val inflationRateByMonths = statisticsService.getInflationRateByMonths(products, ChartPropertiesDto(listOf()))
        assertTrue(inflationRateByMonths.containsKey(AVERAGE_KEY))
    }

    @Test
    fun returnOnlyExistingLabels() {
        val inflationRateByMonths = statisticsService.getInflationRateByMonths(products, ChartPropertiesDto(listOf()))
        assertEquals(1, inflationRateByMonths[AVERAGE_KEY]?.labels?.size)
    }

    @Test
    fun validLabelsFormatting() {
        val inflationRateByMonths = statisticsService.getInflationRateByMonths(products, ChartPropertiesDto(listOf()))
        val date = LocalDate.now()
        val label = "${date.month.toString().substring(0..2)}-${date.year}"
        assertEquals(listOf(label), inflationRateByMonths[AVERAGE_KEY]?.labels)
    }

    @Test
    fun splitCategories() {
        val inflationRateByMonths = statisticsService.getInflationRateByMonths(products, ChartPropertiesDto(listOf(), group = false))
        assertEquals(CATEGORIES, inflationRateByMonths[AVERAGE_KEY]?.dataSeriesList?.size)
    }

    @Test
    fun groupCategories() {
        val inflationRateByMonths = statisticsService.getInflationRateByMonths(products, ChartPropertiesDto(listOf()))
        assertEquals(1, inflationRateByMonths[AVERAGE_KEY]?.dataSeriesList?.size)
    }

    companion object {

        @JvmStatic
        @BeforeAll
        fun setUpAll() {
            for (i in 1..PRODUCTS) {
                val prices = mutableListOf(PriceEntry(date = LocalDate.now(), price = Money(BigDecimal.TEN, BYN)))
                products.add(Product(i.toLong(), "apiKey$i", "name$i", "category${i % CATEGORIES}", prices))
            }
        }
    }
}
