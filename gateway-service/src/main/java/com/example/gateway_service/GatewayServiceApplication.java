package com.example.gateway_service;


import com.example.gateway_service.filter.JwtAuthenticationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}

@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder, JwtAuthenticationFilter jwtAuthenticationFilter) {
	return builder.routes()
			// Route for User Service
			.route("user-route", r -> r.path("/user/**")
			.filters(f -> f.stripPrefix(1)
			.filter(jwtAuthenticationFilter) // JWT authentication filter
			.circuitBreaker(c -> c.setName("CircuitBreaker")
			.setFallbackUri("forward:/fallback"))) // Circuit breaker fallback
			.uri("lb://user-service")) // Load balance to user-service

			// Route for E-commerce Service
			.route("ecommerce-route", r -> r.path("/ecommerce/**")
			.filters(f -> f.stripPrefix(1)
			.filter(jwtAuthenticationFilter)) // JWT authentication filter
			.uri("lb://ecommerce-service")) // Load balance to ecommerce-service

			// Route for Payment Service
			.route("payment-route", r -> r.path("/payment/**")
			.filters(f -> f.stripPrefix(1)
			.filter(jwtAuthenticationFilter)) // JWT authentication filter
			.uri("lb://payment-service")) // Load balance to payment-service

			// Route for Swagger UI (Optional)
			.route("openapi", r -> r.path("/v3/api-docs/**")
			.filters(f -> f.rewritePath("/v3/api-docs/(?<service>.*)", "/${service}/v3/api-docs"))
			.uri("lb://gateway-service")) // Redirect to the current gateway for Swagger docs
			.build();
			}
}
