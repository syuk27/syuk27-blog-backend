package com.syuk27.blog.domain.userpost.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

@Entity
public class UserPostBlock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JsonProperty("cloudImg_url")
	private String cloud_img_url;
	
	@Lob
	private String content;
	
	@ManyToOne
	@JoinColumn(name="post_id", nullable = false)
	private UserPost userPost;
	
	public UserPostBlock() {}

	public UserPostBlock(String cloud_img_url, String content) {
		super();
		this.cloud_img_url = cloud_img_url;
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public String getCloud_img_url() {
		return cloud_img_url;
	}

	public void setCloud_img_url(String cloud_img_url) {
		this.cloud_img_url = cloud_img_url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public UserPost getUserPost() {
		return userPost;
	}

	public void setUserPost(UserPost userPost) {
		this.userPost = userPost;
	}
}
