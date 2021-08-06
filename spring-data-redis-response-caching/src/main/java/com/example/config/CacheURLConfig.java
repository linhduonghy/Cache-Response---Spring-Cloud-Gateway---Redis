package com.example.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.example.cache.URLTree;

public class CacheURLConfig {

	public final Set<String> CACHE_URL = new HashSet<>(
			Arrays.asList("GET/caching-service/*", "GET/first-service/a/*", "GET/second-service/a/*/b"));

	public CacheURLConfig() {
	}

	public URLTree buildURLTree() {
		
		URLTree urlTree = new URLTree();

		for (String url : this.CACHE_URL) {
			
			String[] values = url.split("[/]+");
			
			urlTree.insertTree(values);

		}
		return urlTree;
	}

}
