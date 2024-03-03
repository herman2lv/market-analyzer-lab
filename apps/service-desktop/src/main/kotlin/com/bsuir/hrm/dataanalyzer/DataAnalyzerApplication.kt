package com.bsuir.hrm.dataanalyzer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@SpringBootApplication
class DataAnalyzerApplication

fun main(args: Array<String>) {
    runApplication<DataAnalyzerApplication>(*args)
}
