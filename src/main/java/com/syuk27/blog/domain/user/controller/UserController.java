package com.syuk27.blog.domain.user.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syuk27.blog.domain.user.model.User;
import com.syuk27.blog.domain.user.model.UserRequestDto;
import com.syuk27.blog.domain.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> registerUser(@Valid @RequestBody UserRequestDto user) {
		return ResponseEntity.ok().body(userService.createUser(user));
	}
	
	@GetMapping("/get")
	public ResponseEntity<Optional<User>> getUser(@AuthenticationPrincipal UserDetails userDetails) {
		String userEmail = userDetails.getUsername();
		
		Optional<User> user = Optional.ofNullable(userEmail).filter(email -> !email.isEmpty())
				.flatMap(userService::getUser);
		return ResponseEntity.ok().body(user);
	}
	
	@PostMapping("/update_password")
	public ResponseEntity<User> changePassword(@Valid @RequestBody UserRequestDto user) {
		return ResponseEntity.ok().body(userService.updatePassword(user));
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<User> deleteUser(@RequestBody User user) {
		return ResponseEntity.ok().body(userService.deleteUser(user));
	}
}
