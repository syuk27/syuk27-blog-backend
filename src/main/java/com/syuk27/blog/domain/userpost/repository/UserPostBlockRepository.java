package com.syuk27.blog.domain.userpost.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.syuk27.blog.domain.userpost.model.UserPost;
import com.syuk27.blog.domain.userpost.model.UserPostBlock;
import com.syuk27.blog.domain.userpost.model.UserPostDto.UserPostBlockDto;

public interface UserPostBlockRepository extends JpaRepository<UserPostBlock, Long> {

	//@Query 활용 upb.userPost.id => UserPostBlock 엔티티에서 정의한 userPost를 통해 userPost 엔티티의 id값 조회
	//JPQL에서는 테이블명이 아니라 엔티티명을 사용해야 함 user_post_block → UserPostBlock
	@Query("SELECT upb FROM UserPostBlock upb WHERE upb.userPost.id = :postId ORDER BY block_order ASC")
	List<UserPostBlockDto> findByPostId(@Param("postId") Long postId);
	
	//Spring Data JPA의 메서드 네이밍 전략(Method Query Creation)
	List<UserPostBlock> findByUserPost(UserPost userPost);
}
