package com.syuk27.blog.infrastructure.file;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.syuk27.blog.exception.CustomException;
import com.syuk27.blog.exception.ErrorType;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class FileStorageService {

	private final Path fileStorageLocation;

	private final FileStorageProperties fileStorageProperties;

	public FileStorageService(FileStorageProperties fileStorageProperties) {
		this.fileStorageProperties = fileStorageProperties;

		this.fileStorageLocation = Paths.get(this.fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);

		} catch (Exception e) {
			throw new CustomException(ErrorType.FILE_EX01.getHttpStatus(), ErrorType.FILE_EX01.getMessage());
		}
	}

	public String storeImage(MultipartFile file, HttpServletRequest request) {
		String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

		try {

			// 파일 저장
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			// URL 생성
			String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
			return baseUrl + "/" + fileName;

		} catch (Exception e) {
			throw new CustomException(ErrorType.FILE_EX02.getHttpStatus(), ErrorType.FILE_EX02.getMessage());
		}
	}
}
