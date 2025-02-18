package com.syuk27.blog.domain.userpost.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.syuk27.blog.domain.user.model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;

@Entity
public class UserPost {

	@Id
	@GeneratedValue
	private Integer id;
	
	@Size(min = 10, max = 4000)
	private String description;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private User user;
}
