package com.bsuir.hrm.dataanalyzer.service

import com.bsuir.hrm.dataanalyzer.data.entity.Product
import com.bsuir.hrm.dataanalyzer.service.dto.ChartPropertiesDto
import com.bsuir.hrm.dataanalyzer.service.dto.Dataset

interface StatisticsService {

    fun getInflationRateByMonths(products: List<Product>, properties: ChartPropertiesDto): Map<String, Dataset>

}
