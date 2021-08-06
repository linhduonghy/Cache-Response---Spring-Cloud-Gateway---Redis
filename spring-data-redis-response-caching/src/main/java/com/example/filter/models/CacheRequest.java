package com.example.filter.models;

import java.io.Serializable;

import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CacheRequest implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	String path;
	HttpMethod method;
	MultiValueMap<String, String> queryParams;
}
