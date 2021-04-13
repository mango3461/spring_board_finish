package org.ict.domain;

import java.sql.Date;
import java.util.List;

import lombok.Data;

@Data
public class BoardVO {
	
	private Long bno;
	private String title;
	private String content;
	private String writer;
	private Date regDate;
	private Date updateDate;
	private int replyCount;
	
	private List<BoardAttachVO> attachList;
}
