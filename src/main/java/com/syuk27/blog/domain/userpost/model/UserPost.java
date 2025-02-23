package com.syuk27.blog.domain.userpost.model;

import java.util.List;

import com.syuk27.blog.domain.user.model.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class UserPost {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToMany(mappedBy="userPost", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserPostBlock> UserPostBlockList;
	
	@ManyToOne(fetch = FetchType.LAZY) //참조하는 엔티티를 실제로 사용할 때 로딩 => 성능 최적화
	@JoinColumn(name="user_id", nullable = false) // Foreign Key 설정 조인은 기본 id 찾아서 함
	private User user;
	
	public UserPost() {}
	
	public UserPost(Long id, User user) {
		super();
		this.id = id;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public List<UserPostBlock> getUserPostBlockList() {
		return UserPostBlockList;
	}

	public void setUserPostBlockList(List<UserPostBlock> userPostBlockList) {
		UserPostBlockList = userPostBlockList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
