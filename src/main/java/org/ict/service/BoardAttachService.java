package org.ict.service;

import java.util.List;

import org.ict.domain.BoardAttachVO;

public interface BoardAttachService {

	public void insert(BoardAttachVO vo);
	
	public void delete(String uuid);
	
	public List<BoardAttachVO> findByBno(Long bno);
}
