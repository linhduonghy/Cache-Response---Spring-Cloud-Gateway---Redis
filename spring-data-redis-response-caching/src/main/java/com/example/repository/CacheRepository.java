package com.example.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;

import com.example.filter.models.CacheRequest;
import com.example.filter.models.CacheResponse;

import reactor.core.publisher.Mono;


@Repository
public class CacheRepository {

	@Autowired
	private ReactiveRedisTemplate<CacheRequest, CacheResponse> template;
	
	
	public void addCacheResponse(CacheRequest cacheRequest, CacheResponse response) {		
		template.opsForValue().set(cacheRequest, response).subscribe();		
	}
	
	public Mono<CacheResponse> getCacheResponse(CacheRequest cacheRequest) {
		return template.opsForValue().get(cacheRequest).map(o -> (CacheResponse) o);
	}
	
	
}
