package org.ict.service;

import java.util.List;

import org.ict.domain.Criteria;
import org.ict.domain.ReplyVO;
import org.ict.mapper.BoardMapper;
import org.ict.mapper.ReplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jdk.internal.org.jline.utils.Log;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyMapper mapper;
	
	@Autowired
	private BoardMapper boardMapper;
	
	@Transactional
	@Override
	public void addReply(ReplyVO vo) {
		mapper.create(vo);
		Long bno = (long)vo.getBno();
		boardMapper.updateReplyCount(bno, +1);
	}

	@Override
	public List<ReplyVO> listReply(int bno) {
		return mapper.getList(bno);
	}

	@Override
	public void modifyReply(ReplyVO vo) {
		mapper.update(vo);
	}

	@Transactional
	@Override
	public void removeReply(int rno) {
		Long bno = mapper.getBno(rno);
		mapper.delete(rno);		
		boardMapper.updateReplyCount(bno, -1);
	}

	@Override
	public List<ReplyVO> getListPage(int bno, Criteria cri) {
		return mapper.getListPage(bno, cri);
	}

	@Override
	public int count(int bno) {
		return mapper.count(bno);
	}

}
