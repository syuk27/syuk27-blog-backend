package com.syuk27.blog.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syuk27.blog.domain.user.model.User;
import com.syuk27.blog.domain.user.model.UserRequestDto;
import com.syuk27.blog.domain.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class UserController {

	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@Valid @RequestBody UserRequestDto user) {
		return ResponseEntity.ok().body(userService.registerUser(user));
	}
	
	@PostMapping("/change_password")
	public ResponseEntity<User> changePassword(@Valid @RequestBody UserRequestDto user) {
		return ResponseEntity.ok().body(userService.registerUser(user));
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<User> deleteUser(@RequestBody User user) {
		return ResponseEntity.ok().body(userService.deleteUser(user));
	}
}
