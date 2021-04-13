package org.ict.service;

import java.sql.Date;

import org.ict.domain.LoginDTO;
import org.ict.domain.UserVO;
import org.ict.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper mapper;
	
	@Override
	public UserVO login(LoginDTO dto) throws Exception {
		return mapper.login(dto);
	}

	@Override
	public void joinMember(UserVO vo) {
		mapper.joinMember(vo);
	}

	@Override
	public UserVO getUserInfo(String uid) {
		return mapper.getUserInfo(uid);
	}

	@Override
	public void keepLogin(String uid, String sessionId, Date next) throws Exception  {
		mapper.keepLogin(uid, sessionId, next);
	}
	
	@Override
	public UserVO checkLoginBefore(String value) {
		return mapper.checkUserWithSessionKey(value);
	}
	
}
