package com.bsuir.hrm.dataanalyzer.web

import com.bsuir.hrm.dataanalyzer.service.DataLoaderService
import com.bsuir.hrm.dataanalyzer.service.dto.scraper.PriceDataDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class DataLoaderController(
    private val dataLoaderService: DataLoaderService,
) {

    @GetMapping("/data/{category}")
    fun data(@PathVariable category: String): List<PriceDataDto> {
        return dataLoaderService.getPriceStatistics(category);
    }

}
