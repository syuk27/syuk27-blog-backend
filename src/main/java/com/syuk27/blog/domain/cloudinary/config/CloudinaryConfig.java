package com.syuk27.blog.domain.cloudinary.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {

	@Bean
	public Cloudinary cloudinary(
			@Value("${cloudinary.cloud-name}") String cloudName,
			@Value("${cloudinary.api-key}") String cloudApiKey, 
			@Value("${cloudinary.api-secret}") String cloudApiSecret
	) {
		return new Cloudinary(ObjectUtils.asMap(
				"cloud_name", cloudName,
				"api_key", cloudApiKey, 
				"api_secret", cloudApiSecret
		));
	}
	
}
