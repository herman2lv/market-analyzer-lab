package com.bsuir.hrm.dataanalyzer.service.impl

import com.bsuir.hrm.dataanalyzer.service.dto.Dataset
import com.bsuir.hrm.dataanalyzer.service.ChartService
import com.bsuir.hrm.dataanalyzer.service.ProductService
import com.bsuir.hrm.dataanalyzer.service.StatisticsService
import com.bsuir.hrm.dataanalyzer.service.dto.ChartMetaInfoDto
import com.bsuir.hrm.dataanalyzer.service.dto.ChartPropertiesDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

private val log: Logger = LoggerFactory.getLogger(ChartServiceImpl::class.java)

@Service
class ChartServiceImpl(
    private val productService: ProductService,
    private val statisticsService: StatisticsService,
) : ChartService {

    override fun getDataset(properties: ChartPropertiesDto): Dataset {
        log.debug("Received properties: {}", properties)
        val products = productService.getProductsByCategories(properties.categories)
        log.debug("Got products list of size: {}", products.size)
        return statisticsService.getInflationRateByMonths(products, properties.start, properties.end)
    }

    override fun getMetaInfo(properties: ChartPropertiesDto): ChartMetaInfoDto {
        log.debug("Received properties: {}", properties)
        TODO("Not yet implemented")
    }
}
