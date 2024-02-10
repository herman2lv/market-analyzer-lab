package com.bsuir.hrm.dataanalyzer.client

import com.bsuir.hrm.dataanalyzer.service.dto.Product

interface ProductServiceClient {
    fun getProducts(categories: List<String>): List<Product>
}
