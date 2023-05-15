package com.bsuir.hrm.dataanalyzer.service

import com.bsuir.hrm.dataanalyzer.service.dto.Dataset
import com.bsuir.hrm.dataanalyzer.service.dto.ChartMetaInfoDto
import com.bsuir.hrm.dataanalyzer.service.dto.ChartPropertiesDto

interface ChartService {

    fun getDataset(properties: ChartPropertiesDto): Dataset

    fun getMetaInfo(properties: ChartPropertiesDto): ChartMetaInfoDto
}
