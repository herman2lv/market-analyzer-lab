package com.bsuir.hrm.dataanalyzer.data

import com.bsuir.hrm.dataanalyzer.domain.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository: JpaRepository<Product, Long>
