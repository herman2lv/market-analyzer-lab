package com.bsuir.hrm.dataanalyzer.web

import com.bsuir.hrm.dataanalyzer.domain.CategoryMetaInfo
import com.bsuir.hrm.dataanalyzer.domain.Dataset
import com.bsuir.hrm.dataanalyzer.service.ChartService
import com.bsuir.hrm.dataanalyzer.service.ProductService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ChartController(
    private val chartService: ChartService,
    private val productService: ProductService,
) {

    @PostMapping("/charts")
    fun chart(@RequestBody props: ChartPropertiesDto): Dataset {
        return chartService.getDataset(props)
    }

    @GetMapping("/categories")
    fun categories(): List<CategoryMetaInfo> {
        return productService.getCategoriesMetaInfo()
    }
}
