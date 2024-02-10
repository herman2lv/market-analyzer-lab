package com.bsuir.hrm.dataanalyzer.client.impl

import com.bsuir.hrm.dataanalyzer.client.ProductServiceClient
import com.bsuir.hrm.dataanalyzer.service.dto.Product
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriBuilder

private val log: Logger = LoggerFactory.getLogger(ProductServiceClientImpl::class.java)

@Component
class ProductServiceClientImpl(
    private val webClient: WebClient,
    private val loadBalancerClient: LoadBalancerClient
) : ProductServiceClient {

    override fun getProducts(categories: List<String>): List<Product> {
        log.debug("Sending request to ProductService: {}", categories)

        val instance = loadBalancerClient.choose("SERVICE-PRODUCT")
        log.debug("Choose instance {} of {} with LoadBalancerClient", instance.instanceId, instance.serviceId)

        val response: List<Product> = webClient.get()
            .uri { uri: UriBuilder ->
                uri.scheme(instance.scheme)
                    .host(instance.host)
                    .port(instance.port)
                    .path("/api/v1/data/in")
                    .queryParam("categories", categories)
                    .build()
            }
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<Product>>() {})
            .block() ?: throw RuntimeException()
        log.info("Received response from SERVICE-PRODUCT of size: {}", response.size)
        return response
    }
}
