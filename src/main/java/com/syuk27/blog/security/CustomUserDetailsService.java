package com.syuk27.blog.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.syuk27.blog.domain.user.model.User;
import com.syuk27.blog.domain.user.model.UserRole;
import com.syuk27.blog.domain.user.repository.UserRepository;
import com.syuk27.blog.exception.CustomException;
import com.syuk27.blog.exception.ErrorType;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String userEmail) throws CustomException {
		
		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new CustomException(ErrorType.USER_EX01.getHttpStatus(), ErrorType.USER_EX01.getMessage()));
		
		String role = UserRole.USER.getValue();
		if(user.getRole().toLowerCase().contains("admin")) {
			role = UserRole.ADMIN.getValue();
		}
		
		List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
		
		log.info("user email: {}, authorities: {}", user.getEmail(), authorities);
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
	}
}
