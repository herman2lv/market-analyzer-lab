package com.bsuir.hrm.dataanalyzer.web

import com.bsuir.hrm.dataanalyzer.data.entity.CategoryMetaInfo
import com.bsuir.hrm.dataanalyzer.service.dto.Dataset
import com.bsuir.hrm.dataanalyzer.service.ChartService
import com.bsuir.hrm.dataanalyzer.service.ProductService
import com.bsuir.hrm.dataanalyzer.service.dto.ChartPropertiesDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class DataController(
    private val chartService: ChartService,
    private val productService: ProductService,
) {

    @PostMapping("/charts")
    fun chart(@RequestBody props: ChartPropertiesDto): Map<String, Dataset> {
        return chartService.getDataset(props)
    }

    @GetMapping("/categories")
    fun categories(): List<CategoryMetaInfo> {
        return productService.getCategoriesMetaInfo()
    }
}
