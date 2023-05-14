package com.bsuir.hrm.dataanalyzer.service.impl

import com.bsuir.hrm.dataanalyzer.service.CategoriesService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

private val log: Logger = LoggerFactory.getLogger(CategoriesServiceImpl::class.java)

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
        log.info("Read categories list")
        log.debug("Categories: size={}, content={}", categories.size, categories)
        return categories
    }

}
