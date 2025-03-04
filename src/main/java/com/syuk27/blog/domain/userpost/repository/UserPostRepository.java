package com.syuk27.blog.domain.userpost.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.syuk27.blog.domain.user.model.User;
import com.syuk27.blog.domain.userpost.model.UserPost;
import com.syuk27.blog.domain.userpost.model.UserPostDto;

public interface UserPostRepository extends JpaRepository<UserPost, Long> {
	
		@Query("SELECT COUNT(up) FROM UserPost up WHERE up.user.id = :userId")
		long countByUserId(@Param("id") Long id);

		//@Query 활용 up.user.id => UserPost 엔티티에서 정의한 user를 통해 user 엔티티의 id값 조회
	    //JPQL에서는 테이블명이 아니라 엔티티명을 사용해야 함 user_post → UserPost
		@Query("SELECT up FROM UserPost up WHERE up.user.id = :userId ORDER BY up.createDate")
		Page<UserPostDto> findByUserId(@Param("userId") Long userId, Pageable pageable);
		
		//Spring Data JPA의 메서드 네이밍 전략(Method Query Creation)
		List<UserPost> findByUser(User user);
		
		List<UserPost> findByUser_id(Long userId);
		
		List<UserPost> findWithBlocksByUserId(@Param("userId") Long userId);
}
