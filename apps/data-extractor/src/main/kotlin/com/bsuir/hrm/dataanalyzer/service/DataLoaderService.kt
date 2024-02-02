package com.bsuir.hrm.dataanalyzer.service

import com.bsuir.hrm.dataanalyzer.service.dto.scraper.PriceDataDto

interface DataLoaderService {
    fun getPriceStatistics(category: String): List<PriceDataDto>
}
