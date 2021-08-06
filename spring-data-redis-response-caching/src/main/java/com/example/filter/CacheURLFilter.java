package com.example.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.example.cache.URLTree;
import com.example.config.CacheURLConfig;

import reactor.core.publisher.Mono;

@Component
public class CacheURLFilter implements GlobalFilter, Ordered {

	@Autowired
	CachingFilter cachingFilter;

	private URLTree urlTree;
	
	public CacheURLFilter() {
		this.urlTree = new CacheURLConfig().buildURLTree();
	}
	@Override
	public int getOrder() {
		return -2;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		var path = exchange.getRequest().getPath().value();
		var method = exchange.getRequest().getMethodValue();
		var requestURL = method + path;
		System.err.println(requestURL);				
		
		String[] values = requestURL.split("[/]+");
		
		boolean cached = this.urlTree.checkMatchURL(this.urlTree.getRoot(), values, 0);

		System.err.println("cached: " + cached);
		// request url match cached url
		if (cached) {
			return cachingFilter.filter(exchange, chain);
		}
		 else
		return chain.filter(exchange);
	}

	// check if request in cache_url
	/*
	 * private boolean checkCachedURL(String requestURL, Set<String> cacheURL) {
	 * 
	 * // GET,/example -> GET,/example/ requestURL += '/';
	 * 
	 * char[] request = requestURL.toCharArray(); int len = request.length;
	 * 
	 * StringBuilder currentRequestURL = new StringBuilder();
	 * 
	 * for (int i = 0; i < len; ++i) { char c = request[i]; if (c == '/') { if
	 * (request[i - 1] == '/') { // ignore double slash // continue; } else { //
	 * System.err.println(currentRequestURL.toString() + "/*"); // GET,/example ->
	 * GET,/example/* String url = currentRequestURL.toString() + "/*"; // GET,
	 * /example/* // check request in cache url if (cacheURL.contains(url)) { return
	 * true; } currentRequestURL.append(c); } } else { currentRequestURL.append(c);
	 * } }
	 * 
	 * if (cacheURL.contains(currentRequestURL.substring(0,
	 * currentRequestURL.length() - 1))) { return true; }
	 * 
	 * return false; }
	 */

}
