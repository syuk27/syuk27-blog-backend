package com.syuk27.blog.domain.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.syuk27.blog.domain.post.model.Post;
import com.syuk27.blog.domain.post.model.PostDto;
import com.syuk27.blog.domain.user.model.User;

public interface PostRepository extends JpaRepository<Post, Long> {
	
		@Query("SELECT COUNT(p) FROM Post p WHERE p.user.id = :userId")
		long countByUserId(@Param("id") Long id);

		//@Query 활용 p.user.id => Post 엔티티에서 정의한 user를 통해 user 엔티티의 id값 조회
	    //JPQL에서는 테이블명이 아니라 엔티티명을 사용해야 함 user_post → Post
		@Query("SELECT p FROM Post p WHERE p.user.id = :userId ORDER BY p.createDate")
		Page<PostDto> findByUserId(@Param("userId") String userId, Pageable pageable);
		
		@Query("""
			    SELECT p 
			      FROM Post p
			      JOIN p.user ur
			     WHERE LOWER(ur.role) = 'admin'
			  ORDER BY p.createDate DESC
			""")
		Page<PostDto> findAdminByRole(Pageable pageable);
		
//		@Query("SELECT p, u.nickname FROM Post p JOIN User u ON p.user.id = u.id WHERE p.id = :postId")
		@Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.id = :postId")
		Optional<PostDto> findByPostId(Long postId);
		
		//Spring Data JPA의 메서드 네이밍 전략(Method Query Creation)
		List<Post> findByUser(User user);
		
		List<Post> findByUser_id(String userId);
		
		List<Post> findWithBlocksByUserId(@Param("userId") String userId);
}
