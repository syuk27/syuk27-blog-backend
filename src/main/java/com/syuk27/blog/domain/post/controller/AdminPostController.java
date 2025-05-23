package com.syuk27.blog.domain.post.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.syuk27.blog.domain.post.model.Post;
import com.syuk27.blog.domain.post.model.PostDto;
import com.syuk27.blog.domain.post.service.PostService;
import com.syuk27.blog.infrastructure.file.FileStorageService;

import jakarta.servlet.http.HttpServletRequest;



@RestController
@RequestMapping("/admin/posts")
public class AdminPostController {
	private final PostService postService;
	
	private final FileStorageService fileStorageService;
	
	public AdminPostController(PostService postService, FileStorageService fileStorageService) {
		this.postService = postService;
		this.fileStorageService = fileStorageService;
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
	
	@GetMapping("/detail/{postId}")
	public ResponseEntity<Optional<PostDto>> getPostByPostId(@PathVariable Long postId) {
		return ResponseEntity.ok().body(postService.getPostById(postId));
	}
	
	/** v2 */
	@PostMapping("/v2/images")
	public ResponseEntity<?> createPostV2(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
		
		List<String> imageUrls = fileStorageService.storeImages(files, request);
		return ResponseEntity.ok().body(imageUrls);
	}
	
	@PostMapping("/v2")
	public ResponseEntity<PostDto> createPostV2(@RequestBody Post post) {
		
		
		return ResponseEntity.ok().build();
	}
}
