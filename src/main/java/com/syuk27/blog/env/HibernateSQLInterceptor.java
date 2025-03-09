package com.syuk27.blog.env;

import org.hibernate.resource.jdbc.spi.StatementInspector;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HibernateSQLInterceptor implements StatementInspector {

	@Override
	public String inspect(String sql) {

		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		
		String queryLocation = "Unknown Location";
		for(StackTraceElement element : stackTrace) {
			if (element.getClassName().startsWith("com.syuk27.blog") && 
					element.getFileName().contains("java") &&
					element.getClassName().contains("Service")
				) {
				queryLocation = element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")";
				log.info("Location: {}, Sql: {}", queryLocation, sql);
			}
		}
		
		// 실행 위치 + SQL 출력
		return sql;
	}
}
