package org.ict.service;

import java.util.List;

import org.ict.domain.BoardAttachVO;
import org.ict.domain.BoardVO;
import org.ict.domain.SearchCriteria;
import org.ict.mapper.BoardAttachMapper;
import org.ict.mapper.BoardMapper;
import org.ict.mapper.ReplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j;

// 서비스 구현체에는 인터페이스의 메서드를 구현합니다.
// 이 클래스는 bean-container에 집어넣어야 호출할 수 있습니다.
// 따라서 root-context.xml에 컴포넌트 스캔 범위를 설정해주신다음
// @Service 어노테이션을 붙여서 스캔을 마친 다음부터 사용해주세요.
@Log4j
@Service
public class BoardServiceImpl implements BoardService {

	// mapper쪽 메서드를 호출하려면 mapper클래스를 먼저 선언합니다.
	@Autowired
	private BoardMapper mapper;
	
	@Autowired
	private ReplyMapper replyMapper; 
	
	@Autowired
	private BoardAttachMapper attachMapper;
	
	// 메서드 실행용 테스트코드를 만들어주세요.
	// src/test/java 하위에 org.ict.service 패키지를 만들고
	// BoardServiceTests 클래스파일을 만들어주세요.
	// 내부 멤버변수로 BoardService타입의 service를 선언하고
	// @Autowired로 주입한 후, 하단에 register용 코드를 작성하세요.
	@Transactional
	@Override
	public void register(BoardVO board) {
		mapper.insert(board);
		Long bno = mapper.getMaxBno();
		if(board.getAttachList() == null || board.getAttachList().size() <= 0) {
			return;
		}
		
		board.getAttachList().forEach(attach -> {
			attach.setBno(bno);
			attachMapper.insert(attach);
		});
	}

	// 하단의 모든 메서드를 다 구현해주세요.
	// 위 register를 참고해서, BoardMapper에 있는 메서드를
	// 그대로 옮기기만 하면 됩니다.
	// return 구문이 있는 메서드들은 return 호출; 형태로
	// 그렇지 않은 메서드들은 register처럼 그냥 호출해주세요.
	@Override
	public BoardVO get(Long bno) {
		
		return mapper.read(bno);
	}

	// return type이 boolean인 메서드는
	// int를 리턴하는 mapper쪽 메서드의 결과가 1인지 체크해서
	// 참 거짓을 리턴하도록 합니다.
	@Override
	public boolean modify(BoardVO board) {
		
		attachMapper.deleteAll(board.getBno());
		
		boolean modifyResult = mapper.update(board) == 1;
		
		if(modifyResult && board.getAttachList().size() > 0) {
			board.getAttachList().forEach(attach -> {
				attach.setBno(board.getBno());
				attachMapper.insert(attach);
			});
		}
		
		return mapper.update(board) == 1;
	}
	
	@Transactional
	@Override
	public boolean remove(Long bno) {
		replyMapper.deleteAll(bno);
		
		attachMapper.deleteAll(bno);
		
		return mapper.delete(bno) == 1;
	}

	@Override
	public List<BoardVO> getList() {
		return mapper.getList();
	}

	public List<BoardVO> getListPage(SearchCriteria cri) {
		return mapper.listPage(cri);
	}

	public int getCountPage(SearchCriteria cri) {
		return mapper.countPageNum(cri);
	}

	@Override
	public List<BoardAttachVO> getAttachList(Long bno) {
		log.info("get Attach list: " + bno);
		return attachMapper.findByBno(bno);
	}



}
