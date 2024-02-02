package com.bsuir.hrm.dataanalyzer.web

import com.bsuir.hrm.dataanalyzer.service.DataLoaderService
import com.bsuir.hrm.dataanalyzer.service.dto.scraper.PriceDataDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kotlin.math.min

@RestController
@RequestMapping("/api/v1")
class DataLoaderController(
    private val dataLoaderService: DataLoaderService,
) {

    @GetMapping("/data/{category}")
    fun data(@PathVariable category: String, @RequestParam(defaultValue = "0") offset: Int, @RequestParam(defaultValue = "100") limit: Int): List<PriceDataDto> {
        val safeLimit = min(100, limit)
        return dataLoaderService.getPriceStatistics(category, offset, safeLimit)
    }

}
