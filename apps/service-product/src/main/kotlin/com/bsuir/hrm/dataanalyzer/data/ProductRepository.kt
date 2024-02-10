package com.bsuir.hrm.dataanalyzer.data

import com.bsuir.hrm.dataanalyzer.data.entity.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {

    fun findAllByCategoryIn(categories: List<String>): List<Product>

}
