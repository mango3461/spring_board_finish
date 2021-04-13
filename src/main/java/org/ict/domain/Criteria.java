package org.ict.domain;

import lombok.Data;

@Data
public class Criteria {
	
	private int page;
	private int number;
	
	// Criteria의 기본값이 page 1, number 10이 되도록
	// 생성자를 설정합니다.
	public Criteria() {
		this.page = 1;
		this.number = 10;
	}
	
	// (페이지 - 1) * 페이지당 글숫자가 limit의 시작점입니다.
	// mybatis는 getter를 호출할 수 있습니다.
	public int getPageStart() {
		return (this.page -1) * number;
	}
}
