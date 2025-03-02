package com.syuk27.blog.domain.userpost.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.syuk27.blog.domain.user.model.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserPost {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@CreatedDate //생성 시간 자동 저장
	@Column(updatable = false) // 생성된 후 변경 불가
	private LocalDateTime createDate;
	
	@LastModifiedDate
	private LocalDateTime updateDate;
	
	@OneToMany(mappedBy="userPost", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserPostBlock> userPostBlockList;
	
	@ManyToOne(fetch = FetchType.LAZY) //참조하는 엔티티를 실제로 사용할 때 로딩 => 성능 최적화
	@JoinColumn(name="user_id", nullable = false) // Foreign Key 설정 조인은 기본 id 찾아서 함
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	public List<UserPostBlock> getUserPostBlockList() {
		return userPostBlockList;
	}

	public void setUserPostBlockList(List<UserPostBlock> userPostBlockList) {
		this.userPostBlockList = userPostBlockList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
