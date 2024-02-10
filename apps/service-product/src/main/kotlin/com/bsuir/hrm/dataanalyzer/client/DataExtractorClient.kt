package com.bsuir.hrm.dataanalyzer.client

import com.bsuir.hrm.dataanalyzer.service.dto.PriceDataDto

interface DataExtractorClient {
    fun getPriceStatistics(category: String, offset: Int, limit: Int): List<PriceDataDto>
}
