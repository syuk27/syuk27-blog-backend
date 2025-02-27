package com.syuk27.blog.domain.userpost.model;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserPostDto {
	private Long id;
	
	private List<UserPostBlockDto> userPostBlockList;
	
	public UserPostDto(UserPost userPost) {
		this.id = userPost.getId();
		this.userPostBlockList = userPost.getUserPostBlockList().stream().map(UserPostBlockDto::new)
				.collect(Collectors.toList()); //UserPostBlockDTO::new == block -> new UserPostBlockDto(block)
	}
	
	public Long getId() {
		return id;
	}

	public List<UserPostBlockDto> getUserPostBlockList() {
		return userPostBlockList;
	}

	public void setUserPostBlockList(List<UserPostBlockDto> userPostBlockList) {
		this.userPostBlockList = userPostBlockList;
	}

	public static class UserPostBlockDto {
		private Long id;
		
		@JsonProperty("cloudImg_url")
		private String cloud_img_url;
		
		private String content;
		
		public UserPostBlockDto(UserPostBlock userPostBlock) {
			this.id = userPostBlock.getId();
			this.cloud_img_url = userPostBlock.getCloud_img_url();
			this.content = userPostBlock.getContent();
		}

		public Long getId() {
			return id;
		}

		public String getCloud_img_url() {
			return cloud_img_url;
		}

		public String getContent() {
			return content;
		}
	}
}
