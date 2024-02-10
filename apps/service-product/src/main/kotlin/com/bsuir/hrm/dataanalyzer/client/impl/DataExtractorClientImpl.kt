package com.bsuir.hrm.dataanalyzer.client.impl

import com.bsuir.hrm.dataanalyzer.client.DataExtractorClient
import com.bsuir.hrm.dataanalyzer.service.dto.PriceDataDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriBuilder

private val log: Logger = LoggerFactory.getLogger(DataExtractorClientImpl::class.java)

@Component
class DataExtractorClientImpl(
    private val webClient: WebClient,
    private val loadBalancerClient: LoadBalancerClient
) : DataExtractorClient {

    override fun getPriceStatistics(category: String, offset: Int, limit: Int): List<PriceDataDto> {
        log.debug("Sending request to DataExtractor: {}:{}:{}", category, offset, limit)

        val instance = loadBalancerClient.choose("DATA-EXTRACTOR")
        log.debug("Choose instance {} of {} with LoadBalancerClient", instance.instanceId, instance.serviceId)

        val response: List<PriceDataDto> = webClient.get()
            .uri { uri: UriBuilder ->
                uri.scheme(instance.scheme)
                    .host(instance.host)
                    .port(instance.port)
                    .path("/api/v1/data/$category")
                    .queryParam("limit", limit)
                    .queryParam("offset", offset)
                    .build()
            }
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<PriceDataDto>>() {})
            .block() ?: throw RuntimeException()
        log.info("Received response from DATA-EXTRACTOR of size: {}", response.size)
        return response
    }
}
