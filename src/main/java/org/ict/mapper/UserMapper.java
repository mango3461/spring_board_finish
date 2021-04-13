package org.ict.mapper;

import java.sql.Date;

import org.apache.ibatis.annotations.Param;
import org.ict.domain.LoginDTO;
import org.ict.domain.UserVO;

public interface UserMapper {

	public UserVO login(LoginDTO dto) throws Exception;
	
	public void joinMember(UserVO vo);
	
	public UserVO getUserInfo(String uid);
	
	public void keepLogin(@Param("uid") String uid, 
						@Param("sessionId") String sessionId, 
						@Param("next") Date next);
	
	public UserVO checkUserWithSessionKey(String value);
}
