package com.syuk27.blog.domain.post.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.syuk27.blog.domain.user.model.User;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "제목을 입력해주세요.")
	@Size(min = 8, message = "제목은 최소 8자 이상이어야 합니다.")
	private String title;
	
	@Lob
	private String description;
	
	@Column(nullable = false)
	private Integer category_id;
	
	@CreatedDate //생성 시간 자동 저장
	@Column(updatable = false) // 생성된 후 변경 불가
	private LocalDateTime createDate;
	
	@LastModifiedDate
	private LocalDateTime updateDate;
	
	@OneToMany(mappedBy="post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostBlock> postBlockList;
	
	@ManyToOne(fetch = FetchType.LAZY) //참조하는 엔티티를 실제로 사용할 때 로딩 => 성능 최적화
	@JoinColumn(name="user_id", nullable = false) // Foreign Key 설정 조인은 기본 id 찾아서 함
	private User user;
	
	@PrePersist
	public void prePersist() {
		if(this.category_id == null) {
			this.category_id = 1; //1: blog_post
		}
	}
}
