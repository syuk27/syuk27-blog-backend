package com.syuk27.blog.domain.user.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
	
	@NotBlank(message = "닉네임을 입력해주세요.")
	@Size(min = 2, max = 10, message = "닉네임은 2~10자로 입력해주세요.")
	@Column(unique = true, nullable = false)
	private String nickname;

	@Email(message = "유효한 이메일 주소를 입력하세요.")
	@NotBlank(message = "이메일을 입력해주세요.")
	@Column(unique = true, nullable = false, updatable = false)
	private String email;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "비밀번호는 최소 1개의 문자, 숫자, 특수문자를 포함해야 합니다.")
	private String password;
}
