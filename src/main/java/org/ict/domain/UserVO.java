package org.ict.domain;

import java.sql.Date;

import lombok.Data;

@Data
public class UserVO {

	private String uid;
	private String upw;
	private String uname;
	private int upoint;
	
	private String sessionId;
	private Date sessionLimit;
	
}
