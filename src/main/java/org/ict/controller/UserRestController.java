package org.ict.controller;

import java.util.List;

import org.ict.domain.ReplyVO;
import org.ict.domain.UserVO;
import org.ict.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/*")
public class UserRestController {

	@Autowired
	private UserService service;
	
	@PostMapping(value="/check/{uid}", consumes="application/json",
						produces= {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<UserVO> getIdCheck(@PathVariable String uid){
		
		ResponseEntity<List<ReplyVO>> entity = null;
		
		try {
			UserVO vo = service.getUserInfo(uid);
			return new ResponseEntity<UserVO>(vo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
}
