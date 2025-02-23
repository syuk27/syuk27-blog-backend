package com.syuk27.blog.domain.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.syuk27.blog.domain.user.model.User;
import com.syuk27.blog.domain.user.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public User createUser(User user) {
		if(user.getId() == null) {
			throw new RuntimeException("createUser not exists id: " + user.getId());
		}
		
		return userRepository.save(user);
	}
	
	public Optional<User> getUser(Long id) {
		return userRepository.findById(id);
	}
	
	public User updateUser(User user) {
		return null;
	}
	
	public boolean deleteUser(Long id) {
		if(!userRepository.existsById(id)) {
			return false;
		}
		
		userRepository.deleteById(id);
		
		if(userRepository.existsById(id)) {
			throw new RuntimeException("deleteUser not exists id: " + id);
		}
		
		return true;
	}
}
