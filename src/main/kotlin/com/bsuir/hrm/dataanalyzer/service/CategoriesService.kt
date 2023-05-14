package com.bsuir.hrm.dataanalyzer.service

import com.bsuir.hrm.dataanalyzer.domain.Category

interface CategoriesService {

    fun getAll(): List<Category>

}
