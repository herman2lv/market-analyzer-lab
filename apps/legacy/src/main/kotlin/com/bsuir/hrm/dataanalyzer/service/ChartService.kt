package com.bsuir.hrm.dataanalyzer.service

import com.bsuir.hrm.dataanalyzer.service.dto.ChartPropertiesDto
import com.bsuir.hrm.dataanalyzer.service.dto.Dataset

interface ChartService {

    fun getDataset(properties: ChartPropertiesDto): Map<String, Dataset>

}
