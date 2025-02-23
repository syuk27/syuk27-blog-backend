package com.syuk27.blog.domain.userpost.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.syuk27.blog.domain.userpost.model.UserPost;
import com.syuk27.blog.domain.userpost.repository.UserPostRepository;

import jakarta.transaction.Transactional;

@Service
public class UserPostService {
	
private final UserPostRepository userPostRepository;
	
	public UserPostService(UserPostRepository userPostRepository) {
		this.userPostRepository = userPostRepository;
	}
	
	public UserPost createUserPost(UserPost userPost) {
		if(userPost.getId() == null) {
			throw new RuntimeException("createUserPost not exists id: " + userPost.getId());
		}
		
		return userPostRepository.save(userPost);
	}
	
	public List<UserPost> getUserPostList(Long userId) {
		
		return userPostRepository.findByUserId(userId);
	}
	
	public UserPost getUserPostWithBlock(Long postId) {
		
//		return userPostRepository.findByUserId(postId);
		return null;
	}
	
	@Transactional //변경 감지하여 자동 업데이트, save() 생략 가능
	public UserPost updateUserPost(UserPost userPost) {
		if(userPost.getId() == null) {
			throw new RuntimeException("updateUserPost not exists id: " + userPost.getId());
		}
		
		UserPost updatedUserPost = userPostRepository.findById(userPost.getId())
				.orElseThrow(() -> new IllegalArgumentException("updateUserPost not exists id: " + userPost.getId()));
		
		updatedUserPost.setUserPostBlockList(userPost.getUserPostBlockList());
		
		return updatedUserPost;
	}
	
	public boolean deleteUserPost(Long userPostId) {
		if(!userPostRepository.existsById(userPostId)) {
			return false;
		}
		
		userPostRepository.deleteById(userPostId);
		
		if(userPostRepository.existsById(userPostId)) {
			throw new RuntimeException("deleteUserPost not exists id: " + userPostId);
		}
		
		return true;
	}
}
