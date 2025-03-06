package com.syuk27.blog.domain.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.syuk27.blog.domain.post.model.Post;
import com.syuk27.blog.domain.post.model.PostBlock;
import com.syuk27.blog.domain.post.model.PostDto.PostBlockDto;

public interface PostBlockRepository extends JpaRepository<PostBlock, Long> {

	//@Query 활용 upb.post.id => PostBlock 엔티티에서 정의한 post를 통해 post 엔티티의 id값 조회
	//JPQL에서는 테이블명이 아니라 엔티티명을 사용해야 함 user_post_block → PostBlock
	@Query("SELECT upb FROM PostBlock upb WHERE upb.post.id = :postId ORDER BY block_order ASC")
	List<PostBlockDto> findByPostId(@Param("postId") Long postId);
	
	//Spring Data JPA의 메서드 네이밍 전략(Method Query Creation)
	List<PostBlock> findByPost(Post post);
}
