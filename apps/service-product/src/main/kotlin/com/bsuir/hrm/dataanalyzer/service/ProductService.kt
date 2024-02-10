package com.bsuir.hrm.dataanalyzer.service

import com.bsuir.hrm.dataanalyzer.service.dto.CategoryMetaInfo
import com.bsuir.hrm.dataanalyzer.data.entity.Product
import com.bsuir.hrm.dataanalyzer.service.dto.PriceDataDto

interface ProductService {

    fun getPriceData(category: String): List<PriceDataDto>

    fun getProductsInCategories(categories: List<String>): List<Product>

    fun getCategoriesMetaInfo(): List<CategoryMetaInfo>
}
