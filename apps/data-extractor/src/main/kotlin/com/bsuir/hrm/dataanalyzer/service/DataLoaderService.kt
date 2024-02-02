package com.bsuir.hrm.dataanalyzer.service

import com.bsuir.hrm.dataanalyzer.service.dto.scraper.PriceDataDto

interface DataLoaderService {
    fun getPriceStatistics(category: String, offset: Int = 0, limit: Int = 100): List<PriceDataDto>
}
