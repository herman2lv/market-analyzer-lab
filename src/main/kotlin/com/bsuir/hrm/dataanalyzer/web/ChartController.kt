package com.bsuir.hrm.dataanalyzer.web

import com.bsuir.hrm.dataanalyzer.domain.Dataset
import com.bsuir.hrm.dataanalyzer.service.CategoriesService
import com.bsuir.hrm.dataanalyzer.service.ChartService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ChartController(
    val chartService: ChartService,
    val categoriesService: CategoriesService,
) {


    @PostMapping("/charts")
    fun chart(@RequestBody props: ChartPropertiesDto): Dataset {
        return chartService.getDataset(props)
    }

    @GetMapping("/categories")
    fun categories(): List<String> {
        return categoriesService.getAll()
    }
}
