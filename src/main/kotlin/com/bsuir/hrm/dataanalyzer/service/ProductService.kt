package com.bsuir.hrm.dataanalyzer.service

import com.bsuir.hrm.dataanalyzer.data.entity.CategoryMetaInfo
import com.bsuir.hrm.dataanalyzer.data.entity.Product

interface ProductService {

    fun getProductsByCategories(categories: List<String>): List<Product>

    fun getCategoriesMetaInfo(): List<CategoryMetaInfo>

}
