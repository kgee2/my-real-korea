package com.itwill.my_real_korea.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwill.my_real_korea.dto.user.User;
import com.itwill.my_real_korea.exception.ExistedUserException;
import com.itwill.my_real_korea.exception.PasswordMismatchException;
import com.itwill.my_real_korea.exception.UserNotFoundException;
import com.itwill.my_real_korea.service.user.UserService;

import io.swagger.annotations.ApiOperation;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@ApiOperation(value = "회원가입 폼")
	@GetMapping(value = "/user-write", produces = "application/json;charset=UTF-8")
	public String user_write() {
		return "user-write";
	}
	
	@ApiOperation(value = "회원가입 액션")
	@PostMapping(value = "user-write-action", produces = "application/json;charset=UTF-8")
	public String user_write_action(@ModelAttribute("fuser") User user,Model model) throws Exception {
		String forward_path = "";
		try {
			userService.create(user);
			userService.updateMailKey(user);
			forward_path="redirect:index";
		}catch (ExistedUserException e) {
			model.addAttribute("msg", e.getMessage());
			forward_path="user-write";
		}
		return forward_path;
	}
	
	
	@ApiOperation(value = "로그인 폼")
	@GetMapping(value = "/user-login", produces = "application/json;charset=UTF-8")
	public String user_login() {
		return "user-login";
	}

	@ApiOperation(value = "로그인 액션")
	@PostMapping(value = "user-login-action", produces = "application/json;charset=UTF-8")
	public String user_login_action(@ModelAttribute("fuser") User user, Model model, HttpSession session) throws Exception {
	    String forwardPath = "";
	    try {
	        User loginUser = userService.login(user.getUserId(), user.getPassword());
	        if (loginUser.getMailAuth() != 1) {
	        	session.setAttribute("authUser", loginUser);
	        	forwardPath = "user-auth";
	        } else {
	            session.setAttribute("sUserId", loginUser.getUserId());
	            forwardPath = "redirect:index";
	        }
	    } catch (UserNotFoundException e) {
	        e.printStackTrace();
	        model.addAttribute("msg1", e.getMessage());
	        forwardPath = "user-login";
	    } catch (PasswordMismatchException e) {
	        e.printStackTrace();
	        model.addAttribute("msg2", e.getMessage());
	        forwardPath = "user-login";
	    }
	    return forwardPath;
	}
	
	
	@LoginCheck
	@ApiOperation(value = "메일 인증 폼")
	@GetMapping(value = "/user-auth")
	public String user_auth(HttpServletRequest request) throws Exception {
		String forward_path = "";
		String sUserId=(String)request.getSession().getAttribute("sUserId");
		User loginUser = userService.findUser(sUserId);
		request.setAttribute("loginUser", loginUser);
		forward_path = "user-auth";
		return forward_path;
	}

	@ApiOperation(value = "메일 인증 액션")
	@PostMapping(value = "user-auth-action", produces = "application/json;charset=UTF-8")
	public String user_auth_action(@RequestParam("mailAuthKey") String mailAuthKey, HttpSession session) throws Exception {
	    String forwardPath = "";
	        User authUser = (User)session.getAttribute("authUser");
	        if(authUser.getMailKey() == Integer.parseInt(mailAuthKey)) {
	        	userService.updateMailAuth(authUser);
	        	/*
	        	 * 확인용
	        	System.out.println("mailAuthKey : "+mailAuthKey);
	        	System.out.println("authUser.getMailKey() : "+authUser.getMailKey());
	        	 */
	        	session.removeAttribute("authUser");
	        	forwardPath = "index";
	        }else {
	        	forwardPath = "user-auth";
	        }
	    return forwardPath;
	}
	
	/***************************ID, Password 찾기********************************/

	@GetMapping(value = "/user-find-id", produces = "application/json;charset=UTF-8")
	public String user_find_id() {
		return "user-find-id";
	}
	
	@PostMapping(value = "/user-find-id-action", produces = "application/json;charset=UTF-8")
	public String user_find_id_action(@RequestParam("name") String name, @RequestParam("email") String email, Model model) throws Exception {
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		String userId = userService.findIdByEmailName(email,name);
		if(userId != null) {
			model.addAttribute("userId", userId);
			model.addAttribute("msg", "회원님의 아이디는 "+userId+" 입니다.");
//			System.out.println("userId : "+userId);
		}else {
			model.addAttribute("msg", "일치하는 회원 정보가 없습니다.");
		}
		return "user-find-id";
	}
	
	@GetMapping(value = "/user-find-pw", produces = "application/json;charset=UTF-8")
	public String user_find_pw() throws Exception {
		return "user-find-pw";
	}
	
	@PostMapping(value = "/user-find-pw-action", produces = "application/json;charset=UTF-8")
	public String user_find_pw_action(@RequestParam("userId") String userId, @RequestParam("email") String email, Model model) throws Exception {
		User user = new User();
		user.setUserId(userId);
		user.setEmail(email);
		int matchCount = userService.findIdByIdEmail(userId, email);
		if(matchCount == 1) {
			userService.sendTempPassword(userId, email);
			model.addAttribute("msg", "이메일로 임시 비밀번호가 발송되었습니다.");
		}else {
			model.addAttribute("msg", "일치하는 회원 정보가 없습니다.");
		}
		return "user-find-pw";
	}
	
	/*********************************************************/
	
	@LoginCheck
	@ApiOperation(value = "회원 정보 보기")
	@GetMapping(value = "/user-view", produces = "application/json;charset=UTF-8")
	public String user_view(HttpServletRequest request) throws Exception {
	    HttpSession session = request.getSession();
	    String sUserId = (String) session.getAttribute("sUserId");
	    if (sUserId == null) {
	        session.setAttribute("requestUrl", request.getRequestURL().toString());
	        return "redirect:user-login";
	    }
	    User loginUser = userService.findUser(sUserId);
	    request.setAttribute("loginUser", loginUser);
	    return "user-view";
	}
	
	@LoginCheck
	@PostMapping("/user-modify")
	public String user_modify(HttpServletRequest request) throws Exception {
		String sUserId=(String)request.getSession().getAttribute("sUserId");
		User loginUser=userService.findUser(sUserId);
		request.setAttribute("loginUser", loginUser);
		return "user-modify";
	}
	
	@LoginCheck
	@PostMapping("user-modify-action")
	public String user_modify_action(@ModelAttribute User user,HttpServletRequest request) throws Exception {
		userService.update(user);
		return "redirect:user-view";
	}
	
	
	@LoginCheck
	@PostMapping("user-remove-action")
	public String user_remove_action(HttpServletRequest request) throws Exception {
		String sUserId=(String)request.getSession().getAttribute("sUserId");
		userService.remove(sUserId);
		request.getSession(false).invalidate();
		return "redirect:index";
	}
	
	@LoginCheck
	@RequestMapping("user-logout-action")
	public String user_logout_action(HttpServletRequest request) {
		request.getSession(false).invalidate();
		return "redirect:index";
	}
	
	/***********GET방식요청시 guest_main redirection*********/
	@GetMapping({
				"user-write-action",
				"user-login-action",
				"user-modify",
				"user-modify-action",
				"user-remove-action"
				})
	public String user_get() {
		return "redirect:index";
	}
	
	/****************Local Exception Handler***********/
	@ExceptionHandler(Exception.class)
	public String user_exception_handler(Exception e) {
		return "error";
	}

}





