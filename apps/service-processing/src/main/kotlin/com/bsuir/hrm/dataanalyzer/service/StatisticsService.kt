package com.bsuir.hrm.dataanalyzer.service

import com.bsuir.hrm.dataanalyzer.service.dto.Dataset
import com.bsuir.hrm.dataanalyzer.service.dto.ProcessingProperties

interface StatisticsService {

    fun getDataset(properties: ProcessingProperties): Map<String, Dataset>

}
