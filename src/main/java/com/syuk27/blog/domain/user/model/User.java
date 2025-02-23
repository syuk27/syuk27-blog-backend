package com.syuk27.blog.domain.user.model;

import java.util.List;

import com.syuk27.blog.domain.userpost.model.UserPost;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class User {

	//jakarta.validation.constraints => 유효성 검증 
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //DB에서 AUTO_INCREMENT를 사용하여 ID 자동 증가
	private Long id;
	
	/**
	@Size(min = 2, message = "이름은 2글자 이상이어야 합니다.")
	@NotNull
	@JsonProperty("user_name") // JSON 출력 시 적용될 필드명
    @JsonAlias({"userName", "usrname"})  // JSON 입력 시 허용할 필드명들
	private String name;
	
	@Past(message = "생일은 현재보다 과거의 날짜이어야 합니다.") //현재보다 과거 
	private LocalDate birthDate;
	*/
	
	//jpa에서 일대다(1:N) 관계를 설정할 때 사용, User (1) <-> Post (N), ex) 한 명의 유저(User)는 여러 개의 게시글(Post)을 가질 수 있음
	//{참조 테이블명}_{PK 필드명} 형식으로 외래 키(Foreign Key)를 자동 생성
	//@JoinColumn(name = "외래키_이름") 사용하여 외래 키 이름 변경 가능 => @ManyToOne에서 사용 (외래키 설정이 필요한곳)
	//CascadeType.REMOVE → 부모를 삭제하면 자식도 삭제
	//orphanRemoval = true → 부모가 자식과의 관계를 끊으면 자식이 자동 삭제됨
	//1:N, 부모가 없으면 자식도 없어야 하는 경우, 부모에서 자식을 제거하면, 자식도 자동 삭제되어야 하는 경우
	@OneToMany(mappedBy="user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserPost> userPostList;
	
	//기본 생성자 추가 (Hibernate 필요)
	public User() {}
	
	public User(Long id) {
		super();
		this.id = id;
//		this.name = name;
//		this.birthDate = birthDate;
	}

	public Long getId() {
		return id;
	}

	public List<UserPost> getUserPostList() {
		return userPostList;
	}

	public void setUserPostList(List<UserPost> userPostList) {
		this.userPostList = userPostList;
	}
	
}
