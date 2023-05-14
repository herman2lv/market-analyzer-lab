package com.bsuir.hrm.dataanalyzer.service

import com.bsuir.hrm.dataanalyzer.domain.Product

interface ProductService {

    fun findAllByCategoryIn(categories: List<String>): List<Product>

}
