package org.ict.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ict.domain.Criteria;
import org.ict.domain.PageMaker;
import org.ict.domain.ReplyVO;
import org.ict.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j;


@RestController
@RequestMapping("/replies")
@Log4j
public class ReplyController {

	@Autowired
	private ReplyService service;

	// consumes는 내가 서버에 전달할 데이터의 자료형으로 json을 기술
	// produces는 서버가 나에게 전달할 데이터의 자료형으로 문자열 기술
	@PostMapping(value="", consumes="application/json",
						produces= {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> register(
							@RequestBody ReplyVO vo){
		
		// 먼저 상태코드와 데이터를 함께 전송할 수 있도록
		// ResponseEntity를 미리 선언해둡니다. 
		ResponseEntity<String> entity = null;
		
		try {
			// 전달받은 vo를 이용해 리플 추가
			service.addReply(vo);
			// try블럭내에서 문제 없었을 경우 200코드 전송
			entity = new ResponseEntity<String>(
						"SUCCESS", HttpStatus.OK);		
		} catch (Exception e) {
			// try블럭 내 문제 발생시 catch블럭처리 
			e.printStackTrace();
			entity = new ResponseEntity<String>(
					e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return entity;
	}
	
	@GetMapping(value="/all/{bno}",
				// XML, JSON을 모두 처리하려면 아래와 같이
				// produces에 MediaType을 두 개 이상 넣습니다.
				produces = {MediaType.APPLICATION_XML_VALUE,
							MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<List<ReplyVO>> list(
					//@PathVariable은 url의 {} 내부 이름과
					//일치하도록 파라미터를 입력하면 해당 파라미터에
					//?파라미터명=값 이 전달되듯 전달해줍니다.
					@PathVariable("bno") Integer bno){
		
		ResponseEntity<List<ReplyVO>> entity = null;
		
		try {
			entity = new ResponseEntity<>(
					service.listReply(bno), HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return entity;
	}
	
	// 일반 방식이 아닌 rest방식에서는 삭제로직을 post가 아닌
	// delete 방식으로 요청하기 때문에 @DeleteMapping
	@DeleteMapping(value="/{rno}",
				produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> remove(
			@PathVariable("rno") int rno){
		ResponseEntity<String> entity = null;
		try {
			service.removeReply(rno);
			entity = 
				new ResponseEntity<String>(
						"SUCCESS", HttpStatus.OK);
		} catch(Exception e ) {
			e.printStackTrace();
			entity = 
				new ResponseEntity<>(
					e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return entity;
	}
	
	@RequestMapping(method= {RequestMethod.PUT, 
							RequestMethod.PATCH},
				value= "/{rno}",
				consumes="application/json",
				produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> modify(
		@RequestBody ReplyVO vo, @PathVariable("rno") int rno){
		
		ResponseEntity<String> entity = null;
		
		try {
			vo.setRno(rno);
			service.modifyReply(vo);
			entity = 
				new ResponseEntity<String>(
						"SUCCESS", HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<String>(
					e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return entity;
	}
	
	@GetMapping(value= "/{bno}/{page}",
				produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<Map<String, Object>> getListPage(
		@PathVariable("bno") int bno, @PathVariable("page") int page){
		
		ResponseEntity<Map<String, Object>> entity = null;
		
		Map<String, Object> result = new HashMap<>();
		
		Criteria cri = new Criteria();
		cri.setPage(page);
		log.info(cri);
		
		List<ReplyVO> list = service.getListPage(bno, cri);
		
		int count = service.count(bno);
		
		PageMaker pageMaker = new PageMaker();
		
		pageMaker.setCri(cri);
		pageMaker.setTotalReply(count);
		
		// 1. result에 put 기능으로 위에 생성한 3객체를 넣어주세요.
		result.put("list", list);
		result.put("pageMaker", pageMaker);
		result.put("cri", cri);
		
		// 2. try~catch구문을 이용해서 성공시 200코드로 리턴
		// 실패시 400에러가 발생하도록 해 주세요.
		try {
			entity = new ResponseEntity<Map<String,Object>>(
							result, HttpStatus.OK);
		} catch (Exception e) {
			entity = new ResponseEntity<Map<String, Object>>(
							HttpStatus.BAD_REQUEST);
		}
		
		return entity;
	}
	
	
	
	
	
	
	
	
	
	
	
}
