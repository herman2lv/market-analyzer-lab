package com.bsuir.hrm.dataanalyzer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean

@SpringBootApplication
@EnableDiscoveryClient
class DataAnalyzerApplication {
    @Bean
    fun routeLocator(builder: RouteLocatorBuilder): RouteLocator? {
        return builder.routes()
            .route { p: PredicateSpec ->
                p
                    .path("/api/v1/data/**")
                    .uri("lb://service-processing")
            }
            .route { p: PredicateSpec ->
                p
                    .path("/api/v1/categories/**")
                    .uri("lb://service-product")
            }
            .route { p: PredicateSpec ->
                p
                    .path("/**")
                    .uri("lb://service-desktop")
            }
            .build()
    }
}

fun main(args: Array<String>) {
    runApplication<DataAnalyzerApplication>(*args)
}
