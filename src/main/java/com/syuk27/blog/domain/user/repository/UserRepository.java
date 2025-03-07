package com.syuk27.blog.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.syuk27.blog.domain.user.model.User;

public interface UserRepository extends JpaRepository<User, String> {

	//email 네이밍 규칙
	Optional<User> findByNickname(String nickname);
	
	Optional<User> findByEmail(String email);
	
	Optional<User> findByEmailAndPassword(String email, String password);
}
