package com.bsuir.hrm.dataanalyzer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate

@SpringBootApplication
class DataAnalyzerApplication

fun main(args: Array<String>) {
    runApplication<DataAnalyzerApplication>(*args)
}
