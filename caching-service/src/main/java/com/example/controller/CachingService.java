package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
/*import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;*/
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class CachingService /* implements ApplicationListener<ServletWebServerInitializedEvent> */ {
	
	private String port;
	
	@Autowired
	Environment environment;
	
	@GetMapping("/caching-service")
	public Mono<String> test() {	
		this.port = environment.getProperty("local.server.port");
		return Mono.just("Hello from Caching Service. Port: " + this.port);
	}

	@GetMapping("/caching-service/{segment}")
	public Mono<String> test1(@PathVariable("segment") String segment) {
		this.port = environment.getProperty("local.server.port");
		return Mono.just("Hello from Caching Service. port= " + this.port + " segment= " + segment);
	}

	/*
	 * @Override public void onApplicationEvent(ServletWebServerInitializedEvent
	 * event) { this.port = event.getWebServer().getPort();
	 * 
	 * }
	 */
}
