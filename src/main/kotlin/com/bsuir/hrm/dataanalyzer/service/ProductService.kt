package com.bsuir.hrm.dataanalyzer.service

import com.bsuir.hrm.dataanalyzer.domain.CategoryMetaInfo
import com.bsuir.hrm.dataanalyzer.domain.Product

interface ProductService {

    fun getProductsByCategories(categories: List<String>): List<Product>

    fun getCategoriesMetaInfo(): List<CategoryMetaInfo>

}
