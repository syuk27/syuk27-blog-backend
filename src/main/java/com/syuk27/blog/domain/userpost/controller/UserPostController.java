package com.syuk27.blog.domain.userpost.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syuk27.blog.domain.userpost.model.UserPost;
import com.syuk27.blog.domain.userpost.model.UserPostDto;
import com.syuk27.blog.domain.userpost.service.UserPostService;



@RestController
@RequestMapping("/user_posts")
public class UserPostController {
	private final UserPostService userPostService;
	
	public UserPostController(UserPostService userPostService) {
		this.userPostService = userPostService;
	}
	
	@PostMapping("")
	public ResponseEntity<UserPostDto> createUserPost(@RequestBody UserPost userPost) {
		return ResponseEntity.ok(userPostService.createUserPost(userPost));
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<List<UserPost>> getAllUserPost(@PathVariable Long userId) {
		return ResponseEntity.ok(userPostService.getUserPostList(userId));
	}
}
