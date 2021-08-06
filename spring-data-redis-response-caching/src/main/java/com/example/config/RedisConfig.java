package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import com.example.filter.models.CacheRequest;
import com.example.filter.models.CacheResponse;

@Configuration
public class RedisConfig {

	@Bean
	public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
		return new LettuceConnectionFactory();
	}
	
	@Bean
	public ReactiveRedisTemplate<CacheRequest, CacheResponse> reactiveRedisTemplate(
			ReactiveRedisConnectionFactory redisConnectionFactory) {

		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
		Jackson2JsonRedisSerializer<CacheRequest> requestSerializer = new Jackson2JsonRedisSerializer<>(CacheRequest.class);
		Jackson2JsonRedisSerializer<CacheResponse> responseSerializer = new Jackson2JsonRedisSerializer<>(CacheResponse.class);
		
		RedisSerializationContext.RedisSerializationContextBuilder<CacheRequest, CacheResponse> builder = RedisSerializationContext
				.newSerializationContext(serializer);
		RedisSerializationContext<CacheRequest, CacheResponse> context = builder.key(requestSerializer).value(responseSerializer)
                .hashKey(requestSerializer).hashValue(responseSerializer).build();
		
		return new ReactiveRedisTemplate<CacheRequest, CacheResponse>(redisConnectionFactory, context);
	}
}
