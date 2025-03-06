package com.syuk27.blog.domain.post.model;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostDto {
	private Long id;
	
	private List<PostBlockDto> postBlockList;
	
	public PostDto(Post post) {
		this.id = post.getId();
		this.postBlockList = post.getPostBlockList().stream().map(PostBlockDto::new)
				.collect(Collectors.toList()); //PostBlockDTO::new == block -> new PostBlockDto(block)
	}
	
	public Long getId() {
		return id;
	}

	public List<PostBlockDto> getPostBlockList() {
		return postBlockList;
	}

	public void setPostBlockList(List<PostBlockDto> postBlockList) {
		this.postBlockList = postBlockList;
	}

	public static class PostBlockDto {
		private Long id;
		
		@JsonProperty("cloudImg_url")
		private String cloud_img_url;
		
		private String content;
		
		public PostBlockDto(PostBlock postBlock) {
			this.id = postBlock.getId();
			this.cloud_img_url = postBlock.getCloud_img_url();
			this.content = postBlock.getContent();
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
