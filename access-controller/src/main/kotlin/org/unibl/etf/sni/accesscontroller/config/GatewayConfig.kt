package org.unibl.etf.sni.accesscontroller.config

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.filters
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GatewayConfig {

    @Bean
    fun gateway(routeLocatorBuilder: RouteLocatorBuilder): RouteLocator {
        val apiPrefix = "/api/v1/"
        return routeLocatorBuilder
            .routes {
                route {
                    path("${apiPrefix}**")
                    filters {
                        tokenRelay()
                        rewritePath("${apiPrefix}(?<segment>.*)", "/$\\{segment}")
                    }
                    uri("http://127.0.0.1:8081")
                }
                route {
                    path("/**")
                    uri("http://127.0.0.1:4200")
                }
            }
    }
}