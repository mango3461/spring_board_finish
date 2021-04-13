package org.ict.domain;

import lombok.Data;

@Data
public class PageMaker {

	private int totalBoard;
	private int startPage;
	private int endPage;
	private boolean prev;
	private boolean next;
	private int totalReply;
	
	// 페이지당 버튼을 몇 개씩 생성할지
	private int displayPageNum;
	
	// 현재 조회중인 페이지를 알아야 버튼을 생성할 수 있음
	private Criteria cri;
	
	// 페이지가 바뀔 때마다 버튼 갯수 및 범위를 산출하는 메서드
	public void calcData() {
		this.displayPageNum = 10;
		
		this.endPage = (int)(Math.ceil(cri.getPage() /
			(double) displayPageNum) *  displayPageNum);
		
		this.startPage = (endPage - displayPageNum) + 1;
		
		int tempEndPage = (int)(Math.ceil(totalBoard /
				(double)cri.getNumber()));
		if(endPage > tempEndPage) {
			endPage = tempEndPage;
		}
		
		prev = startPage == 1 ? false : true;
		
		next = endPage * cri.getNumber() >= 
						totalBoard ? false : true;
		
	}
	// 페이지가 바뀔 때마다 버튼 갯수 및 범위를 산출하는 메서드
	public void calcData2() {
		this.displayPageNum = 10;
		
		this.endPage = (int)(Math.ceil(cri.getPage() /
				(double) displayPageNum) *  displayPageNum);
		
		this.startPage = (endPage - displayPageNum) + 1;
		
		int tempEndPage = (int)(Math.ceil(totalReply /
				(double)cri.getNumber()));
		if(endPage > tempEndPage) {
			endPage = tempEndPage;
		}
		
		prev = startPage == 1 ? false : true;
		
		next = endPage * cri.getNumber() >= 
				totalReply ? false : true;
		
	}
	
	public void setTotalBoard(int totalBoard) {
		this.totalBoard = totalBoard;
		
		calcData();
	}
	
	public void setTotalReply(int totalReply) {
		this.totalReply = totalReply;
		
		calcData2();
	}
	
	
	
	
}
