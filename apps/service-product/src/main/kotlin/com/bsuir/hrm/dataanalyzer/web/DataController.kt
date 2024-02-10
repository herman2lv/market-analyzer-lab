package com.bsuir.hrm.dataanalyzer.web

import com.bsuir.hrm.dataanalyzer.data.entity.Product
import com.bsuir.hrm.dataanalyzer.service.ProductService
import com.bsuir.hrm.dataanalyzer.service.dto.CategoryMetaInfo
import com.bsuir.hrm.dataanalyzer.service.dto.PriceDataDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class DataController(
    private val productService: ProductService,
) {

    @GetMapping("/data/{category}")
    fun data(@PathVariable category: String): List<PriceDataDto> {
        return productService.getPriceData(category)
    }

    @GetMapping("/data/in")
    fun dataIn(@RequestParam categories: List<String>): List<Product> {
        return productService.getProductsInCategories(categories)
    }

    @GetMapping("/categories")
    fun categories(): List<CategoryMetaInfo> {
        return productService.getCategoriesMetaInfo()
    }

}
