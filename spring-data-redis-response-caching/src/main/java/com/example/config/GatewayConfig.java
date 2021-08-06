package com.example.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p.path("/caching-service/**").uri("lb://CACHING-SERVICE"))
				.route(p -> p.path("/first-service/**").uri("lb://FIRST-SERVICE"))
				.route(p -> p.path("/second-service/**").uri("lb://SECOND-SERVICE"))
				.build();
	}
}
