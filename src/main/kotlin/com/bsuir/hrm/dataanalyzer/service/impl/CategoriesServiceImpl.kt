package com.bsuir.hrm.dataanalyzer.service.impl

import com.bsuir.hrm.dataanalyzer.domain.Category
import com.bsuir.hrm.dataanalyzer.service.CategoriesService
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

private val log: Logger = LoggerFactory.getLogger(CategoriesServiceImpl::class.java)

@Service
class CategoriesServiceImpl(
    val jsonMapper: ObjectMapper,
) : CategoriesService {

    private lateinit var categories: List<Category>

    override fun getAll(): List<Category> {
        if (!this::categories.isInitialized) {
            categories = getAllInternal()
        }
        return categories
    }

    private fun getAllInternal(): List<Category> {
        val reader = javaClass.getResourceAsStream("/categories.json") ?: throw RuntimeException("Configuration error")
        reader.use {
            val json: JsonNode = jsonMapper.readTree(reader.readAllBytes())
            log.info("Read categories list")
            log.debug("Categories: size={}, content={}", json.size(), json)
            return json.map { Category(it["key"].asText(), it["display"].asText()) }
        }
    }

}
