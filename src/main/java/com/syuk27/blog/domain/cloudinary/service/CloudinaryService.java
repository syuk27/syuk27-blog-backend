package com.syuk27.blog.domain.cloudinary.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;

@Service
public class CloudinaryService {

	private final Cloudinary cloudinary;
	
	public CloudinaryService(Cloudinary cloudinary) {
		this.cloudinary = cloudinary;
	}

	public Map<String, Object> generateSignature() throws Exception {
		long timestamp = System.currentTimeMillis() / 1000;
		Map<String, Object> paramsToSign = new HashMap<>();
		paramsToSign.put("timestamp", timestamp);
		
		String signature = this.cloudinary.apiSignRequest(paramsToSign, this.cloudinary.config.apiSecret);
		
		Map<String, Object> response = new HashMap<>();
		response.put("timestamp", timestamp);
		response.put("signature", signature);
		response.put("api_key", this.cloudinary.config.apiKey);
		
		return response;
	}
}
