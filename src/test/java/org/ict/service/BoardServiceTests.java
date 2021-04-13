package org.ict.service;

import java.util.List;

import org.ict.domain.BoardVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
"file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class BoardServiceTests {
	
	@Autowired
	private BoardService service;
	
	//@Test
	public void testRegister() {
		// BoardVO를 생성하고 service의 register 메서드를 사용
		// 해주세요.
		// 어렵다면 BoardMapperTests를 참고해서 완성시켜주세요.
		BoardVO board = new BoardVO();
		
		board.setTitle("서비스제목");
		board.setContent("서비스본문");
		board.setWriter("서비스글쓴이");
		
		service.register(board);
	}
	
	//@Test
	public void testGet() {
		service.get(3L);
	}
	
	//@Test
	public void testModify() {
		// 실제 수정로직은 수정 이전에
		// 1. 특정 글의 전체 내용을 가져와 수정창에 뿌린다.
		// 2. 뿌려진 내용 중 수정하고 싶은 내용을 수정한다.
		// 3. 수정된 내용을 제출버튼을 통해 최종 반영한다.
		// 따라서 위 로직을 따라가기 위해 이번에는
		// BoardVO를 생성하는 대신
		// get메서드를 활용해서 실제 글 내용을 먼저 가져온다음
		// 수정된 내용이 DB에 반영되도록 하겠습니다.
		BoardVO board = service.get(4L);

		// get에 입력한 번호의 글이 없는 경우 자동 중지
		if (board == null) {
			return;
		}
		
		board.setTitle("Modify반영제목");
		board.setContent("Modify반영본문");
		
		service.modify(board);
		
	}
	
	//@Test
	public void testGetList() {
		//한 번에 처리하는 코드
		//service.getList().forEach(board -> {
		//	log.info(board);
		//});
		
		// 두 단계로 나눠서 처리하는 코드
		// 1. 전체 글 목록을 List<BoardVO> 에 담는다.
		List<BoardVO> boards = service.getList();
		// 2. 가져온 게시판 글 뭉치 boards를 forEach 처리한다
		boards.forEach(board -> {
			log.info(board);
		});
	}
	
	@Test
	public void testDelete() {
		service.remove(1L);
	}
	

}




