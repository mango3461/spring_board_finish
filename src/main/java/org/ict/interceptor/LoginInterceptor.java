package org.ict.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.log4j.Log4j;

@Log4j
public class LoginInterceptor extends HandlerInterceptorAdapter{

	private static final String LOGIN = "login";

	@Override
	public void postHandle(HttpServletRequest request,
							HttpServletResponse response,
							Object handler,
							ModelAndView modelAndView) throws Exception{
		
		//세션을 컨트롤하기 위해 세션정보를 받아옵니다.
		HttpSession session = request.getSession();
		
		// model.addAttribute로 올라가있을 유저정보를 가져옵니다.
		ModelMap modelMap = modelAndView.getModelMap();
		Object userVO = modelMap.get("userVO");
		
		//로그인 상태 점검을 위해 userVO가 실제로 등록된 데이터인지 확인합니다.
		if(userVO != null) {
			log.info("new login success");
			session.setAttribute(LOGIN, userVO);
			
			if(request.getParameter("useCookie") != null) {
				log.info("remember !");
				Cookie loginCookie = new Cookie("loginCookie", session.getId());
				loginCookie.setPath("/");
				loginCookie.setMaxAge(60 * 60 * 24 * 7);
				response.addCookie(loginCookie);
			}
			
			//response.sendRedirect("/");
			// saveDest() 로 인해 세션에 저장된 이전 페이지 정보 가져오기
			Object dest = session.getAttribute("dest");
			
			response.sendRedirect(dest != null ? (String)dest : "/");
		}
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request,
							HttpServletResponse response,
							Object handler) throws Exception{
		
		//세션 정보 가져오기
		HttpSession session = request.getSession();
		
		// 세션에 기존 로그인 정보가 있다면 말소시키기
		if(session.getAttribute(LOGIN) != null) {
			log.info("clear login data before");
			session.removeAttribute(LOGIN);
		}
		return true;
	}
	
	
}
