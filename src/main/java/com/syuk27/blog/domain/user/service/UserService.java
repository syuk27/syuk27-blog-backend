package com.syuk27.blog.domain.user.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.syuk27.blog.domain.user.model.User;
import com.syuk27.blog.domain.user.model.UserRequestDto;
import com.syuk27.blog.domain.user.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public User createUser(UserRequestDto userRequestDto) {
		if(userRepository.findByNickname(userRequestDto.getNickname()).isPresent()) {
			throw new RuntimeException("USEREXCEPTION02");
		}
		
		if(userRepository.findByEmail(userRequestDto.getEmail()).isPresent()) {
			throw new RuntimeException("USEREXCEPTION02");
		}
		
		String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());
		
		User user = new User();
		user.setNickname(userRequestDto.getNickname());
		user.setEmail(userRequestDto.getEmail());
		user.setPassword(encodedPassword);
		
		return userRepository.save(user);
	}
	
	public Optional<User> getUser(String userEmail) {
		return userRepository.findByEmail(userEmail);
	}
	
	public User updatePassword(UserRequestDto userRequestDto) {
		Optional<User> userOptional = userRepository.findByEmail(userRequestDto.getEmail());
		
		if(!userOptional.isPresent()) {
			throw new RuntimeException("USEREXCEPTION01");
		}
		
		if(passwordEncoder.matches(userRequestDto.getPassword(), userOptional.get().getPassword())) {
			throw new RuntimeException("USEREXCEPTION03");
		}
		
		User user = new User();
		user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
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
