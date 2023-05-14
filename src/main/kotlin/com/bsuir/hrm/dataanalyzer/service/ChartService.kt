package com.bsuir.hrm.dataanalyzer.service

import com.bsuir.hrm.dataanalyzer.domain.Dataset
import com.bsuir.hrm.dataanalyzer.web.ChartMetaInfoDto
import com.bsuir.hrm.dataanalyzer.web.ChartPropertiesDto

interface ChartService {

    fun getDataset(properties: ChartPropertiesDto): Dataset

    fun getMetaInfo(properties: ChartPropertiesDto): ChartMetaInfoDto
}
