package com.example.filter;

import java.nio.charset.Charset;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;

import com.example.filter.models.CacheRequest;
import com.example.filter.models.CacheResponse;
import com.example.service.CacheService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CachingFilter implements GatewayFilter, Ordered {

	final Logger logger = LoggerFactory.getLogger(CachingFilter.class);

	@Autowired
	private CacheService cacheService;

	@Override
	public int getOrder() {
		return -2; // -1 is response write filter, must call before that
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		var cacheRequest = getCacheRequest(exchange.getRequest());

		var cacheResponsePublisher = cacheService.getCacheResponse(cacheRequest);
			
		return cacheResponsePublisher.hasElement().flatMap(ok -> {
			if(!ok) {
				System.err.println("No response in cache");
				var decorateResponse = getDecorateResponse(exchange, cacheRequest);
				return chain.filter(exchange.mutate().response(decorateResponse).build());
			} else {
				return cacheResponsePublisher.flatMap(cacheResponse -> {
					System.err.println(cacheResponse);
					logger.info("Return cache response for request: {}", cacheRequest);
					
					var serverHttpResponse = exchange.getResponse();
					serverHttpResponse.setStatusCode(cacheResponse.getHttpStatus());
					serverHttpResponse.getHeaders().addAll(cacheResponse.getHeaders());
					var buffer = serverHttpResponse.bufferFactory().wrap(cacheResponse.getBody());

					return serverHttpResponse.writeWith(Flux.just(buffer));
				});
			}
		});
//		if (cacheR.hasElement().equals(ok)) {
//			return cacheR.flatMap(cacheResponse -> {
//				System.err.println(cacheResponse);
//				logger.info("Return cache response for request: {}", cacheRequest);
//				var serverHttpResponse = exchange.getResponse();
//				serverHttpResponse.setStatusCode(cacheResponse.getHttpStatus());
//				serverHttpResponse.getHeaders().addAll(new HttpHeaders());
//				var buffer = serverHttpResponse.bufferFactory().wrap(cacheResponse.getBody());			
//				return serverHttpResponse.writeWith(Flux.just(buffer));			
//			});
//		}
//		return getResponse(exchange, chain, cacheRequest);
		
		
//		return cacheService.getCacheResponse(cacheRequest).flatMap(cacheResponse -> {
//			System.err.println(cacheResponse);
//			logger.info("Return cache response for request: {}", cacheRequest);
//			var serverHttpResponse = exchange.getResponse();
//			serverHttpResponse.setStatusCode(cacheResponse.getHttpStatus());
//			serverHttpResponse.getHeaders().addAll(new HttpHeaders());
//			var buffer = serverHttpResponse.bufferFactory().wrap(cacheResponse.getBody());			
//			return serverHttpResponse.writeWith(Flux.just(buffer));			
//		}).then().switchIfEmpty(Mono.defer(() -> getResponse(exchange, chain, cacheRequest)));
		
//		CacheResponse cacheResponse = new CacheResponse(null, null, null);
//		CacheResponse cacheResponse = null;
//		System.err.println(cacheResponse);
//		if (Objects.nonNull(cacheResponse)) { // exist response of request in cache
//
//			logger.info("Return cache response for request: {}", cacheRequest);
////
//			var serverHttpResponse = exchange.getResponse();
//			serverHttpResponse.setStatusCode(cacheResponse.getHttpStatus());
//			serverHttpResponse.getHeaders().addAll(new HttpHeaders());
//			var buffer = serverHttpResponse.bufferFactory().wrap(cacheResponse.getBody());
//
//			return serverHttpResponse.writeWith(Flux.just(buffer));
//
//		}

		// not exist response of request in cache
		// read response and save in cache
//		var decorateResponse = getDecorateResponse(exchange, cacheRequest);
//		return chain.filter(exchange.mutate().response(decorateResponse).build());
	}

	private ServerHttpResponseDecorator getDecorateResponse(ServerWebExchange exchange, CacheRequest cacheRequest) {

		var originResponse = exchange.getResponse();
		var dataBufferFactory = originResponse.bufferFactory();

		return new ServerHttpResponseDecorator(originResponse) {

			@Override
			public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

				if (body instanceof Flux) {
					var flux = (Flux<? extends DataBuffer>) body;
					return super.writeWith(flux.map(dataBuffer -> {

						// get body in response
						byte[] content = new byte[dataBuffer.readableByteCount()];
						dataBuffer.read(content);
						// save response to cache
						if (getStatusCode().is2xxSuccessful()) {
							var cachedResponse = CacheResponse.builder().httpStatus(getStatusCode())
									.headers(getHeaders()).body(content).build();
							logger.info("Request {} cached response {}", exchange.getRequest().getPath(),
									new String(cachedResponse.getBody(), Charset.forName("UTF-8")));

							cacheService.addCacheResponse(cacheRequest, cachedResponse);
						}
						return dataBufferFactory.wrap(content);
					}));
				}
				return super.writeWith(body);
			}
		};
	}

	private CacheRequest getCacheRequest(ServerHttpRequest request) {
		return CacheRequest.builder().method(request.getMethod()).path(request.getPath().value())
				.queryParams(request.getQueryParams()).build();
	}

}
