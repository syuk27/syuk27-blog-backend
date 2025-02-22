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
	@JoinColumn(name="post_id")
	private UserPost userPost;
	
	public UserPostBlock() {}

	public UserPostBlock(Long id, String cloud_img_url, String content) {
		super();
		this.id = id;
		this.cloud_img_url = cloud_img_url;
		this.content = content;
	}
}
