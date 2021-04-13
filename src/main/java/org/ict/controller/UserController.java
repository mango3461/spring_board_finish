package org.ict.controller;

import java.sql.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ict.domain.LoginDTO;
import org.ict.domain.UserVO;
import org.ict.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

@Controller
@RequestMapping("/user/*")
public class UserController {

	@Autowired
	private UserService service;
	
	@GetMapping("/login")
	public void loginGet(@ModelAttribute("dto") LoginDTO dto) {
		
	}
	
	@PostMapping("/loginPost")
	public void loginPost(LoginDTO dto, HttpSession session, Model model)
		throws Exception{
		
		UserVO vo = service.login(dto);
		
		if(vo == null) {
			return;
		}
		model.addAttribute("userVO", vo);
		
		if(dto.isUseCookie()) {
			int amount = 60 * 60 * 24 * 7;
			
			Date sessionLimit = new Date(System.currentTimeMillis() + (1000 * amount));
			
			service.keepLogin(vo.getUid(), session.getId(), sessionLimit);
		}
	}
	
	@GetMapping("/joinmember")
	public void joinMember() {
		
	}
	@PostMapping("/joinmember")
	public String joinMember(UserVO vo) {
		service.joinMember(vo);
		return "redirect:/board/list";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request,
						HttpServletResponse response,
						HttpSession session) throws Exception {
		
		Object obj = session.getAttribute("login");
		
		if(obj != null) {
			UserVO vo = (UserVO) obj;
			
			session.removeAttribute("login");
			session.invalidate();
			
			Cookie loginCookie = WebUtils.getCookie(request, "loginCookie");
			
			if(loginCookie != null) {
				loginCookie.setPath("/");
				loginCookie.setMaxAge(0);
				response.addCookie(loginCookie);
				service.keepLogin(vo.getUid(), session.getId(), new Date(System.currentTimeMillis()));
			}
		}
		return "user/logout";
	}
	
	
	
}

