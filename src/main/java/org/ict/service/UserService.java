package org.ict.service;

import java.sql.Date;

import org.ict.domain.LoginDTO;
import org.ict.domain.UserVO;

public interface UserService {

	public UserVO login(LoginDTO dto) throws Exception;
	
	public void joinMember(UserVO vo);
	
	public UserVO getUserInfo(String uid);
	
	public void keepLogin(String uid, String sessionId, Date next) throws Exception;
	
	public UserVO checkLoginBefore(String value);
}
