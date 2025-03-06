package com.syuk27.blog.domain.post.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.syuk27.blog.domain.post.model.Post;
import com.syuk27.blog.domain.post.model.PostDto;
import com.syuk27.blog.domain.user.model.User;

public interface PostRepository extends JpaRepository<Post, Long> {
	
		@Query("SELECT COUNT(up) FROM Post up WHERE up.user.id = :userId")
		long countByUserId(@Param("id") Long id);

		//@Query 활용 up.user.id => Post 엔티티에서 정의한 user를 통해 user 엔티티의 id값 조회
	    //JPQL에서는 테이블명이 아니라 엔티티명을 사용해야 함 user_post → Post
		@Query("SELECT up FROM Post up WHERE up.user.id = :userId ORDER BY up.createDate")
		Page<PostDto> findByUserId(@Param("userId") String userId, Pageable pageable);
		
		@Query("""
			    SELECT up 
			      FROM Post up
			      JOIN up.user ur
			     WHERE LOWER(ur.role) = 'admin'
			  ORDER BY up.createDate
			""")
		Page<PostDto> findAdminByRole(Pageable pageable);
		
		//Spring Data JPA의 메서드 네이밍 전략(Method Query Creation)
		List<Post> findByUser(User user);
		
		List<Post> findByUser_id(String userId);
		
		List<Post> findWithBlocksByUserId(@Param("userId") String userId);
}
