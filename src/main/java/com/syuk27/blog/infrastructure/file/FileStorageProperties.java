package com.syuk27.blog.infrastructure.file;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "file") //application.properties 또는 application.yml에서 file.로 시작하는 설정을 자동으로 읽어서 Java 클래스 필드에 매핑
@Getter
@Setter
public class FileStorageProperties {

	private String uploadDir;
}