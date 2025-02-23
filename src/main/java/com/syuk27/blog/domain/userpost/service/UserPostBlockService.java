package com.syuk27.blog.domain.userpost.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.syuk27.blog.domain.userpost.model.UserPostBlock;
import com.syuk27.blog.domain.userpost.repository.UserPostBlockRepository;

import jakarta.transaction.Transactional;

@Service
public class UserPostBlockService {

	private final UserPostBlockRepository userPostBlockRepository;
	
	public UserPostBlockService(UserPostBlockRepository userPostBlockRepository) {
		this.userPostBlockRepository = userPostBlockRepository;
	}
	
	public UserPostBlock createUserPostBlock(UserPostBlock userPostBlock) {
		if(userPostBlock.getId() == null) {
			throw new RuntimeException("createUserPost not exists id: " + userPostBlock.getId());
		}
		
		return userPostBlockRepository.save(userPostBlock);
	}
	
	public List<UserPostBlock> getUserPostBlockList(Long postId) {
		
		return userPostBlockRepository.findByPostId(postId);
	}
	
	@Transactional //변경 감지하여 자동 업데이트, save() 생략 가능
	public UserPostBlock updateUserPostBlock(UserPostBlock userPostBlock) {
		if(userPostBlock.getId() == null) {
			throw new RuntimeException("updateUserPostBlock not exists id: " + userPostBlock.getId());
		}
		
		UserPostBlock updatedUserPostBlock = userPostBlockRepository.findById(userPostBlock.getId())
				.orElseThrow(() -> new IllegalArgumentException("updateUserPostBlock not exists id: " + userPostBlock.getId()));
		
		updatedUserPostBlock.setCloud_img_url(userPostBlock.getCloud_img_url());
		updatedUserPostBlock.setContent(userPostBlock.getContent());
		
		return updatedUserPostBlock;
	}
	
	public boolean deleteUserPostBlock(Long userPostBlockId) {
		if(!userPostBlockRepository.existsById(userPostBlockId)) {
			return false;
		}
		
		userPostBlockRepository.deleteById(userPostBlockId);
		
		if(userPostBlockRepository.existsById(userPostBlockId)) {
			throw new RuntimeException("deleteUserPostBlock not exists id: " + userPostBlockId);
		}
		
		return true;
	}
}
