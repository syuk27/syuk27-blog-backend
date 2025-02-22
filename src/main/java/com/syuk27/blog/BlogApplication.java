package com.syuk27.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.syuk27.blog.env.EnvLoader;

@SpringBootApplication
public class BlogApplication {

	public static void main(String[] args) {
		EnvLoader.loadEnv(); // .env 파일 로드
		SpringApplication.run(BlogApplication.class, args);
	}

}
