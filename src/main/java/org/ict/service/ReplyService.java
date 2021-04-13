package org.ict.service;

import java.util.List;

import org.ict.domain.Criteria;
import org.ict.domain.ReplyVO;

public interface ReplyService {

	public void addReply(ReplyVO vo);
	
	public List<ReplyVO> listReply(int bno);
	
	public void modifyReply(ReplyVO vo);
	
	public void removeReply(int rno);
	
	public List<ReplyVO> getListPage(int bno, Criteria cri);
	
	public int count(int bno);
	
}
