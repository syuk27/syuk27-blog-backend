package com.syuk27.blog.domain.userpost.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.syuk27.blog.domain.userpost.model.UserPost;
import com.syuk27.blog.domain.userpost.model.UserPostBlock;
import com.syuk27.blog.domain.userpost.model.UserPostDto;
import com.syuk27.blog.domain.userpost.model.UserPostDto.UserPostBlockDto;
import com.syuk27.blog.domain.userpost.repository.UserPostBlockRepository;
import com.syuk27.blog.domain.userpost.repository.UserPostRepository;

import jakarta.transaction.Transactional;

@Service
public class UserPostService {

	private final UserPostRepository userPostRepository;
	private final UserPostBlockRepository userPostBlockRepository;

	public UserPostService(UserPostRepository userPostRepository, UserPostBlockRepository userPostBlockRepository) {
		this.userPostRepository = userPostRepository;
		this.userPostBlockRepository = userPostBlockRepository;
	}

	@Transactional
	public UserPostDto createUserPost(UserPost userPost) {
		Optional.ofNullable(userPost.getUserPostBlockList())
				.ifPresent(blocks -> blocks.forEach(block -> block.setUserPost(userPost)));

		// forEach는 isEmpty() 체크 필요없음 -> 빈 리스트일 경우 동작 안함
		// 순환 참조(무한 루프) 해결 dto 사용
		return new UserPostDto(userPostRepository.save(userPost));
	}

	public Page<UserPostDto> getUserPostList(Long userId, Pageable pageable) {
		Page<UserPostDto> userPostList = userPostRepository.findByUserId(userId, pageable);
		
		Optional.ofNullable(userPostList).ifPresent(userPosts -> userPosts.forEach(userPost -> {
			List<UserPostBlockDto> userPostBlockList = userPostBlockRepository.findByPostId(userPost.getId());
			userPost.setUserPostBlockList(userPostBlockList);
		}));

		return userPostList;
	}

	@Transactional // 변경 감지하여 자동 업데이트, save() 생략 가능
	public UserPost updateUserPost(UserPost userPost) {
		if (userPost.getId() == null) {
			throw new RuntimeException("updateUserPost not exists id: " + userPost.getId());
		}

		UserPost updatedUserPost = userPostRepository.findById(userPost.getId())
				.orElseThrow(() -> new IllegalArgumentException("updateUserPost not exists id: " + userPost.getId()));

		updatedUserPost.setUserPostBlockList(userPost.getUserPostBlockList());

		return updatedUserPost;
	}

	public boolean deleteUserPost(Long userPostId) {
		if (!userPostRepository.existsById(userPostId)) {
			return false;
		}

		userPostRepository.deleteById(userPostId);

		if (userPostRepository.existsById(userPostId)) {
			throw new RuntimeException("deleteUserPost not exists id: " + userPostId);
		}

		return true;
	}

	@Transactional // 변경 감지하여 자동 업데이트, save() 생략 가능
	public UserPostBlock updateUserPostBlock(UserPostBlock userPostBlock) {
		if (userPostBlock.getId() == null) {
			throw new RuntimeException("updateUserPostBlock not exists id: " + userPostBlock.getId());
		}

		UserPostBlock updatedUserPostBlock = userPostBlockRepository.findById(userPostBlock.getId()).orElseThrow(
				() -> new IllegalArgumentException("updateUserPostBlock not exists id: " + userPostBlock.getId()));

		updatedUserPostBlock.setCloud_img_url(userPostBlock.getCloud_img_url());
		updatedUserPostBlock.setContent(userPostBlock.getContent());

		return updatedUserPostBlock;
	}
}
