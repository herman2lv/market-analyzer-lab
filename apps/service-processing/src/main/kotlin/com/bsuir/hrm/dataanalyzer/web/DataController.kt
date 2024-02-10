package com.bsuir.hrm.dataanalyzer.web

import com.bsuir.hrm.dataanalyzer.service.StatisticsService
import com.bsuir.hrm.dataanalyzer.service.dto.Dataset
import com.bsuir.hrm.dataanalyzer.service.dto.ProcessingProperties
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class DataController(
    private val statisticsService: StatisticsService
) {

    @PostMapping("/data")
    fun chart(@RequestBody props: ProcessingProperties): Map<String, Dataset> {
        return statisticsService.getDataset(props)
    }

}
