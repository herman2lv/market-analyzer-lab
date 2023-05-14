package com.bsuir.hrm.dataanalyzer.service.impl

import com.bsuir.hrm.dataanalyzer.service.CategoriesService
import org.springframework.stereotype.Service

@Service
class CategoriesServiceImpl : CategoriesService {

    private lateinit var categories: List<String>

    override fun getAll(): List<String> {
        if (!this::categories.isInitialized) {
            categories = getAllInternal()
        }
        return categories
    }

    private fun getAllInternal(): List<String> {
        val reader = javaClass.getResourceAsStream("/categories.txt") ?: throw RuntimeException("Configuration error")
        var categories: List<String>
        reader.use {
            val content = String(reader.readAllBytes())
            categories = content.split(Regex("\\s+")).filter { it.isNotBlank() }
        }
        return categories
    }

}
