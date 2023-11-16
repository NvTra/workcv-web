package com.tranv.workcv.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tranv.workcv.dto.Registerdto;
import com.tranv.workcv.service.UserService;

@Controller
@RequestMapping("auth")
public class AuthController {
	@Autowired
	private UserService userService;
	
	// Handle the request to show the login page.
	@GetMapping("/login")
	public String login() {
		return "/public/login";
	}

	// Handle the request to show the login form.
	@GetMapping("/showFormLogin")
	public String showFormLogin() {
		return "/public/login";
	}

	// Handle the request after successful logout.
	@GetMapping("/logoutSuccessful")
	public String logoutSuccessfulPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}
		return "/public/home";
	}

	// Handle the request to show the access denied page.
	@GetMapping("/access-denied")
	public String accessDeniel() {
		return "access-denied";
	}
	
	/**
	 * Add a new user. This method saves the new user using the userService and
	 * redirects the user to the home page.
	 */

	@PostMapping("/register")
	public String addUser(@ModelAttribute("newUser") Registerdto newUser) {
		System.out.println(newUser.toString());
		userService.saveUser(newUser);
		return "redirect:/";
	}
}
