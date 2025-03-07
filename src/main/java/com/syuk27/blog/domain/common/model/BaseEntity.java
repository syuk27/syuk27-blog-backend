package com.syuk27.blog.domain.common.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass // 모든 엔티티에서 상속 가능
@EntityListeners(AuditingEntityListener.class) // 엔티티 리스너 연결 자동으로 createdAt, updatedAt을 관리
public class BaseEntity {
	
	@CreatedDate // 생성 시간 자동 저장
	@Column(updatable = false, nullable = false) // 생성된 후 변경 불가
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 입력 변환 => 밀리초 제거 안됨 @PrePersist 사용해서 제거 해야 될듯 
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") // 출력 변환
	private LocalDateTime createDate;

	@LastModifiedDate
	@Column(insertable = false) // insert 시에는 포함 x 저장은 안되지만 저장 후 반환 json에 들어있음
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updateDate;
}
