package com.example.filter.models;

import java.io.Serializable;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CacheResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	HttpStatus httpStatus;
	HttpHeaders headers;
	byte[] body;
	
	
}
