package com.bsuir.hrm.dataanalyzer.configuration

import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@EnableDiscoveryClient
class AppConfig {
    @Bean
    fun webClient(): WebClient {
        return WebClient.builder().build()
    }
}
