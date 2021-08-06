package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstServiceController {

	@GetMapping("/first-service/**")
	public String get() {
		return "Hello from First Service";
	}
	
//	@GetMapping("/first-service/a/{a}")
//	public String get(@PathVariable("a") String a) {
//		return "Hello from First Service: a=" + a;
//	}
//	@GetMapping("/first-service/a/{a}/b")
//	public String get1(@PathVariable("a") String a) {
//		return "Hello from First Service: a=" + a;
//	}
//	@GetMapping("/first-service/a/{a}/b/{b}")
//	public String get(@PathVariable("a") String a, @PathVariable("b") String b) {
//		return "Hello from First Service: a=" + a + ", b=" + b;
//	}
}
