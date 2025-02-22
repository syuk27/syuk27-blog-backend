package com.syuk27.blog.domain.cloudinary.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syuk27.blog.domain.cloudinary.service.CloudinaryService;

@RestController
@RequestMapping("/api/cloudinary")
public class CloudinaryController {

	private final CloudinaryService cloudinaryService;
	
	public CloudinaryController(CloudinaryService cloudinaryService) {
		this.cloudinaryService = cloudinaryService;
	}
	
	@GetMapping("/signature")
	public ResponseEntity<Map<String, Object>> getSignature() throws Exception {
		
		Map<String, Object> getSignature = cloudinaryService.generateSignature();
		
		return ResponseEntity.ok().body(getSignature);
	}
}
