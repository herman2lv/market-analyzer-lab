package com.bsuir.hrm.dataanalyzer.service.impl

import com.bsuir.hrm.dataanalyzer.domain.Dataset
import com.bsuir.hrm.dataanalyzer.service.ChartService
import com.bsuir.hrm.dataanalyzer.service.ProductService
import com.bsuir.hrm.dataanalyzer.service.StatisticsService
import com.bsuir.hrm.dataanalyzer.web.ChartMetaInfoDto
import com.bsuir.hrm.dataanalyzer.web.ChartPropertiesDto
import org.springframework.stereotype.Service

@Service
class ChartServiceImpl(
    val productService: ProductService,
    val statisticsService: StatisticsService,
) : ChartService {

    override fun getDataset(properties: ChartPropertiesDto): Dataset {
        val products = productService.findAllByCategoryIn(properties.categories)
        return statisticsService.getInflationRateByMonths(products)
    }

    override fun getMetaInfo(properties: ChartPropertiesDto): ChartMetaInfoDto {
        TODO("Not yet implemented")
    }
}
