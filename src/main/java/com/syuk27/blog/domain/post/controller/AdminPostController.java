package com.syuk27.blog.domain.post.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syuk27.blog.domain.post.model.Post;
import com.syuk27.blog.domain.post.model.PostDto;
import com.syuk27.blog.domain.post.service.PostService;



@RestController
@RequestMapping("/admin/posts")
public class AdminPostController {
	private final PostService postService;
	
	public AdminPostController(PostService postService) {
		this.postService = postService;
	}
	
	@PostMapping("")
	public ResponseEntity<PostDto> createPost(@RequestBody Post post) {
		return ResponseEntity.ok(postService.createPost(post));
	}
	
	@GetMapping("/{page}")
	public ResponseEntity<Page<PostDto>> getAllPost(@PathVariable Integer page) {
//		page는 0 부터 시작
		Pageable pageable = PageRequest.of(page - 1, 12);
		return ResponseEntity.ok(postService.getPostList(pageable));
	}
}
