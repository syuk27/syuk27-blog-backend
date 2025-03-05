package com.syuk27.blog.domain.user.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.syuk27.blog.domain.user.model.User;
import com.syuk27.blog.domain.user.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public User registerUser(User user) {
		if(userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new RuntimeException("USEREXCEPTION02");
		}
		
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		return userRepository.save(user);
	}
	
	public User changePassword(User user) {
		Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
		
		if(!userOptional.isPresent()) {
			throw new RuntimeException("USEREXCEPTION01");
		}
		
		if(passwordEncoder.matches(user.getPassword(), userOptional.get().getPassword())) {
			throw new RuntimeException("USEREXCEPTION03");
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
	
	public User deleteUser(User user) {
		Optional<User> userOptional = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
		if(!userOptional.isPresent()) {
			throw new RuntimeException("USEREXCEPTION01");
		}
		
		userRepository.deleteById(userOptional.get().getId());
		return userOptional.get();
	}
}
