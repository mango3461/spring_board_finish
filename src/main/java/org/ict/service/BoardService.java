package org.ict.service;

import java.util.List;

import org.ict.domain.BoardAttachVO;
import org.ict.domain.BoardVO;
import org.ict.domain.SearchCriteria;

// 서비스 계층에는 일반적으로 담당테이블 하나당 인터페이스 하나
// 그리고 그 인터페이스를 구현한 구현클래스 하나를 만듭니다.
public interface BoardService {

	public void register(BoardVO board);
	
	public BoardVO get(Long bno);
	
	public boolean modify(BoardVO board);
	
	public boolean remove(Long bno);
	
	public List<BoardVO> getList();
	
	public List<BoardVO> getListPage(SearchCriteria cri);

	public int getCountPage(SearchCriteria cri);
	
	public List<BoardAttachVO> getAttachList(Long bno);
}



