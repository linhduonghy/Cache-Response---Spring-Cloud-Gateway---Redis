package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecondServiceController {

	@GetMapping("/second-service")
	public String get() {
		return "Hello from Second Service";
	}
	
	@GetMapping("/second-service/{segment}")
	public String get1(@PathVariable("segment") String segment) {
		return "Hello from Second Service: segment= " + segment;
	}
}
