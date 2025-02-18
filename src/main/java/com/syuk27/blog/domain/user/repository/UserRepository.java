package com.syuk27.blog.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.syuk27.blog.domain.user.model.User;


public interface UserRepository extends JpaRepository<User, Integer> {


}
