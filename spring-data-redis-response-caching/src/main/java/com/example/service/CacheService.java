package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.filter.models.CacheRequest;
import com.example.filter.models.CacheResponse;
import com.example.repository.CacheRepository;

import reactor.core.publisher.Mono;

@Service
public class CacheService {

	@Autowired 
	private CacheRepository repository;
	
	public void addCacheResponse(CacheRequest cacheRequest, CacheResponse response) {		
		repository.addCacheResponse(cacheRequest, response);
	}
	
	public Mono<CacheResponse> getCacheResponse(CacheRequest cacheRequest) {
		return repository.getCacheResponse(cacheRequest);
	}
	
}
