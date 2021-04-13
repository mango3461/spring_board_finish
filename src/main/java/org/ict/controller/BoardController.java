package org.ict.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.ict.domain.BoardAttachVO;
import org.ict.domain.BoardVO;
import org.ict.domain.Criteria;
import org.ict.domain.PageMaker;
import org.ict.domain.SearchCriteria;
import org.ict.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.log4j.Log4j;

// 컨트롤러는 기본적으로 테이블 단위로 생성합니다.
// 가령 게시판 테이블 관련 컨트롤러는 아래와 같이 만들고
// 여기에 회원가입 기능이 추가되면
// MemberContorller가 추가되는 식입니다.
// bean-container에 추가하기 위해 어노테이션을 붙입니다.
@Controller
// 만약 하단 @Log4j에 오류가 발생하면
// pom.xml의 log4j의 scope 태그를 주석처리합니다.
@Log4j
@RequestMapping("/board/*")
public class BoardController {
	
	// 컨트롤러에서 특정 주소에 접속했을때, 서비스 메서드를
	// 실행하기 위해서 객체 선언 및 자동주입을 해 주세요.
	@Autowired
	private BoardService service;
	
	// "/list" 주소를 가지는 list 메서드를 만들어주세요.
	// void 리턴을 하며, .addAttribute()를 이용해
	// list라는 이름으로 전체 글 목록을 뷰에 전달합니다.
	@RequestMapping("/list")
	// Criteria를 선언하면 /board/list 주소에서
	// page, number 파라미터로 값 전달 가능
	public void list(Model model, SearchCriteria cri) {
		
		log.info("list");
		//model.addAttribute("list", service.getList());
		model.addAttribute("list", 
				service.getListPage(cri));
		model.addAttribute("cri", cri);
		
		//페이지네이터를 그리기 위해 처리 정보 전달
		PageMaker pageMaker = new PageMaker();
		// 현재 몇 페이지를 조회중인지 알아야 설정이 되므로
		pageMaker.setCri(cri);
		//pageMaker.setTotalBoard(131);
		pageMaker.setTotalBoard(service.getCountPage(cri));
		model.addAttribute("pageMaker", pageMaker);
	}
	
	// CRUD(select, insert, delete, update)기능 연결시
	// SELECT를 제외한 기능에는 특별한 상황이 아니면
	// Post방식을 적용합니다.
	// 따라서 PostMapping 어노테이션을 써야합니다.
	@PostMapping("/register")
	public String register(BoardVO board,
							SearchCriteria cri,
							RedirectAttributes rttr) {
		// 게시물 등록 후 리스트 창으로 이동하기 위해
		// 리다이렉트 방식을 활용합니다.
		// 리다이렉트가 된 이후에 쓸 데이터를 남기기 위해
		// Model.addAttribute() 대신
		// RedirectAttributes를 활용하고 이 경우는
		// Redirect이후에도 전달데이터가 남습니다.
		service.register(board);
		
		rttr.addFlashAttribute("result", board.getBno());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("keyword", cri.getKeyword());
		
		log.info("======================");
		log.info("register: " + board);
		if(board.getAttachList() != null) {
	
			board.getAttachList().forEach(attach -> log.info(attach));
		}
		
		return "redirect:/board/list";
	}
	
	// get방식으로 해당 주소 접속시 글 작성창으로 안내
	@GetMapping("/register")
	public String register() {
		return "/board/register";
	}
	
	// 특정 번호의 글을 상세페이지에서 조회할 수 있도록
	// get 기능을 구현해보겠습니다.
	// 이 메서드는 파라미터로 글 번호 bno를 입력받고
	// model.addAttribute() 기능을 이용해 특정 글 번호에
	// 해당하는 글 내용을 .jsp파일로 보내줍니다.
	// 메서드의 리턴 자료형은 void입니다.
	// 가능하시면 view 파일도 생성해서 상세글보기 페이지를
	// 구현해보세요.
	// 지금 현재 메서드가 출력할 .jsp 파일을 생성하고,
	// 그 파일 내부에 bno번 글을 표출해주세요.
	@GetMapping("/get")
	public void get(Long bno, Model model, 
			SearchCriteria cri){
		model.addAttribute("cri", cri);
		model.addAttribute("board", service.get(bno));
	}
	
	// 수정 기능을 담당하는 modify메서드를 만들어보겠습니다.
	// 수정 창으로 접근하는 .jsp는 get방식으로 접근하지만
	// 실제 수정이 이루어지는 로직은 post방식으로 만듭니다.
	// 수정창으로 진입하는 부분과, 수정 후 디테일 페이지로
	// 넘어오는 로직을 추가로 작성해주세요.
	@PostMapping("/modify")
	public String modify(Model model, 
			SearchCriteria cri, Long bno) {
		
		// 계획
		// 1. 상세 글 정보를 저장합니다.
		BoardVO board = service.get(bno);
		// 2. 이 정보를 view 파일로 전송합니다.
		model.addAttribute("board", board);
		
		model.addAttribute("cri", cri);
		
		return "/board/modify";
	}
	
	// form에서 넘어온 파라미터를 받기 위해 
	// 메서드 선언부에 BoardVO를 선언하고
	// 리다이렉트시 글번호를 같이 넘기기 위해 
	// RedirectAttributes를 선언합니다.
	@PostMapping("/modifyrun")
	public String modify(BoardVO board,
					SearchCriteria cri,
					RedirectAttributes rttr) {
		// 넘겨받은 글 정보를 갱신 등록
		service.modify(board);
		
		// 수정된 글 번호 정보를 저장
		rttr.addFlashAttribute("bno",board.getBno());
		rttr.addAttribute("page", cri.getPage());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("keyword", cri.getKeyword());
		
		// 디테일 페이지로 넘어가기 위해 redirect 주소 설정
		return "redirect:/board/get?bno=" + board.getBno();
	}
	
	// Remove 로직을 아래에 짜 주세요.
	// 일반테스트시는 get방식을 처리할 수 있도록,
	// 이후 테스트코드로 테스트할때는 post방식을 처리할수있도록 합니다.
	@PostMapping("/remove")
	public String remove(Long bno, 
				RedirectAttributes rttr, 
				SearchCriteria cri,
				Model model) {
		
		// 먼저 첨부파일 정보를 attachList 변수에 저장
		List<BoardAttachVO> attachList = service.getAttachList(bno);
		
		// 글 삭제 로직이 정상적으로 처리된 경우에만 첨부파일들을 실제 폴더에서 삭제합니다.
		if(service.remove(bno)) {
			deleteFiles(attachList);
		}
				
		// 추후 삭제 완료시 XX번 글이 삭제되었습니다라는
		// 팝업을 띄우기 위해 미리 세팅
		rttr.addFlashAttribute("bno", bno);
		//model.addAttribute("bno", bno);
		
		// Criteria의 page 정보를 url에 붙여서 보냅니다.
		rttr.addAttribute("page", cri.getPage());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("keyword", cri.getKeyword());
		// 삭제된 글의 디테일페이지는 존재하지 않으므로 리스트로 이동
		return "redirect:/board/list";
	}
	
	@GetMapping(value="/getAttachList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<BoardAttachVO>> getAttachList(Long bno){
		return new ResponseEntity<>(service.getAttachList(bno), HttpStatus.OK);
	}

	// List<boardAttachVO>를 받아서, 해당 사진을 전부 폴더에서 삭제함.
	private void deleteFiles(List<BoardAttachVO> attachList) {
		if(attachList == null || attachList.size() == 0) {
			return;
		}
		
		log.info(attachList);
		
		attachList.forEach(attach -> {
			try {
				// java.nio에 내장된 경로지정 API
				Path file = Paths.get("c:\\upload_data\\temp\\" + attach.getUploadPath() + 
									"\\" + attach.getUuid() + "_" + attach.getFileName());
				Files.deleteIfExists(file);
				if(Files.probeContentType(file).startsWith("image")) {
					Path thumbNail = Paths.get("c:\\upload_data\\temp\\" + attach.getUploadPath() + 
									"\\s_" + attach.getUuid() + "_" + attach.getFileName());
					Files.delete(thumbNail);
				}
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		});//foreach
	}
}





