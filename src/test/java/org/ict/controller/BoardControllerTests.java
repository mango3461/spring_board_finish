package org.ict.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
// 컨트롤러 테스트시에 추가로 붙는 어노테이션
@WebAppConfiguration
// 컨트롤러는 실제 웹사이트 접속처럼 처리하기 때문에 서버관련 설정이 된
// servlet-context.xml도 같이 인식시켜야 작동함
@ContextConfiguration({
"file:src/main/webapp/WEB-INF/spring/root-context.xml",
"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"
})
@Log4j
public class BoardControllerTests {
	
	// 접속모듈 자동주입
	@Autowired
	private WebApplicationContext ctx;
	
	// 가상브라우저 선언
	private MockMvc mockMvc;
	
	
	// 테스트코드 실행 전 우선 mockMvc 객체를 초기화합니다.
	@Before
	public void setup() {
		this.mockMvc = 
	MockMvcBuilders.webAppContextSetup(ctx).build();
	}
	
	// 본격적인 테스트는 위 항목들을 전부 작성한 이후에 작성합니다.
	// MockMvcRequestBuilders.get("주소")
	// MockMvcRequestBuilders.post("주소")
	// 와 같은 방식으로 접속방식을 조절할 수 있습니다.
	//@Test
	public void testList() throws Exception {
		
		mockMvc.perform(
			MockMvcRequestBuilders.get("/board/list"))
					.andReturn()
					.getModelAndView()
					.getModelMap();
	}
	
	// 만약 해당 주소에 파라미터를 실어서 보내는 경우는
	// 아래와 같이 .post("주소") 뒤에
	// .param("보낼컬럼명", "보낼데이터") 를 연결하고
	// 파라미터가 여럿인 경우 연달아 이어서 .param()을 작성합니다.
	//@Test
	public void testRegister() throws Exception {
		mockMvc.perform(
		  MockMvcRequestBuilders.post("/board/register")
		  	.param("title", "Mock제목")
		  	.param("content", "Mock본문")
		  	.param("writer", "Mock글쓴이")
		).andReturn().getModelAndView().getViewName();
	}
	
	// 직접 /board/get 주소에 대한 테스트코드를 작성해주세요.
	// get방식으로 처리했던 testList()와
	// param을 발송했던 testRegister()를 조합하면
	// 테스트코드를 짤 수 있습니다.
	// .param()으로 넘기는 파라미터는 url이므로
	// 설령 숫자를 전송하는 상황이어도 무조건 문자열 처리합니다.
	//@Test
	public void testGet() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get("/board/get")
						.param("bno", "7"))
						.andReturn()
						.getModelAndView()
						.getModelMap();
	}
	
	
	// testRegister와 유사하게 실행되나, 쿼리문 구조가 다르므로
	// 맞는 자료를 전달하도록 파라미터와 url만 간단히 수정하면
	// 똑같이 작동함.
	//@Test
	public void testModify() throws Exception {
		mockMvc.perform(
		  MockMvcRequestBuilders.post("/board/modify")
		  	.param("title", "Mock테스트제목수정")
		  	.param("content", "Mock테스트본문수정")
		  	.param("bno", "7")
		).andReturn().getModelAndView().getViewName();
	}
	
	// remove로직 역시 .post에 url을 적고 파라미터로
	// bno와 번호만 전달하면 삭제를 잘 수행합니다.
	@Test
	public void testRemove() throws Exception {
		mockMvc.perform(
		  MockMvcRequestBuilders.post("/board/remove")
		  	.param("bno", "8")
		).andReturn().getModelAndView().getViewName();
	}
	
}






