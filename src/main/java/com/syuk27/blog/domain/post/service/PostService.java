package com.syuk27.blog.domain.post.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.syuk27.blog.domain.post.model.Post;
import com.syuk27.blog.domain.post.model.PostBlock;
import com.syuk27.blog.domain.post.model.PostDto;
import com.syuk27.blog.domain.post.model.PostDto.PostBlockDto;
import com.syuk27.blog.domain.post.repository.PostBlockRepository;
import com.syuk27.blog.domain.post.repository.PostRepository;

import jakarta.transaction.Transactional;

@Service
public class PostService {

	private final PostRepository postRepository;
	private final PostBlockRepository postBlockRepository;

	public PostService(PostRepository postRepository, PostBlockRepository postBlockRepository) {
		this.postRepository = postRepository;
		this.postBlockRepository = postBlockRepository;
	}

	@Transactional
	public PostDto createPost(Post post) {
		Optional.ofNullable(post.getPostBlockList())
				.ifPresent(blocks -> blocks.forEach(block -> block.setPost(post)));

		// forEach는 isEmpty() 체크 필요없음 -> 빈 리스트일 경우 동작 안함
		// 순환 참조(무한 루프) 해결 dto 사용
		return new PostDto(postRepository.save(post));
	}

	//user post
	public Page<PostDto> getPostList(String userId, Pageable pageable) {
		Page<PostDto> postList = postRepository.findByUserId(userId, pageable);
		return setPostBlockListForPosts(postList);
	}
	
	//admin post
	public Page<PostDto> getPostList(Pageable pageable) {
		Page<PostDto> postList = postRepository.findAdminByRole(pageable);
		return setPostBlockListForPosts(postList);
	}
	
	public Optional<PostDto> getPostById(Long postId) {
		Optional<PostDto> post = postRepository.findByPostId(postId);
		post.ifPresent(tmpPost -> {
			List<PostBlockDto> postBlockList = postBlockRepository.findByPostId(postId);
			tmpPost.setPostBlockList(postBlockList);
		});
		return post;
	}
	
	private Page<PostDto> setPostBlockListForPosts(Page<PostDto> postList) {
	    Optional.ofNullable(postList).ifPresent(posts -> posts.forEach(post -> {
	        List<PostBlockDto> postBlockList = postBlockRepository.findByPostId(post.getId());
	        post.setPostBlockList(postBlockList);
	    }));
	    return postList;
	}
	
	@Transactional // 변경 감지하여 자동 업데이트, save() 생략 가능
	public Post updatePost(Post post) {
		if (post.getId() == null) {
			throw new RuntimeException("updatePost not exists id: " + post.getId());
		}

		Post updatedPost = postRepository.findById(post.getId())
				.orElseThrow(() -> new IllegalArgumentException("updatePost not exists id: " + post.getId()));

		updatedPost.setPostBlockList(post.getPostBlockList());

		return updatedPost;
	}

	public boolean deletePost(Long postId) {
		if (!postRepository.existsById(postId)) {
			return false;
		}

		postRepository.deleteById(postId);

		if (postRepository.existsById(postId)) {
			throw new RuntimeException("deletePost not exists id: " + postId);
		}

		return true;
	}

	@Transactional // 변경 감지하여 자동 업데이트, save() 생략 가능
	public PostBlock updatePostBlock(PostBlock postBlock) {
		if (postBlock.getId() == null) {
			throw new RuntimeException("updatePostBlock not exists id: " + postBlock.getId());
		}

		PostBlock updatedPostBlock = postBlockRepository.findById(postBlock.getId()).orElseThrow(
				() -> new IllegalArgumentException("updatePostBlock not exists id: " + postBlock.getId()));

		updatedPostBlock.setCloud_img_url(postBlock.getCloud_img_url());
		updatedPostBlock.setContent(postBlock.getContent());

		return updatedPostBlock;
	}
}
