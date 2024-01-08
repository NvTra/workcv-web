package com.tranv.workcv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tranv.workcv.entity.User;
import com.tranv.workcv.service.UserService;
import com.tranv.workcv.until.PaginationUtil;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private UserService userService;

	@GetMapping("/manager-user")
	private String managerUser(@RequestParam(name = "page", defaultValue = "1") int currentPage, Model model) {
		List<User> listUser = userService.getListUsers();
		PaginationUtil.pagination(listUser, currentPage, model);
		return "admin/manager-user";
	}

	@GetMapping("/lock")
	private String lockUser(@RequestParam("userId") int userId) {
		userService.lockUser(userId);
		return "redirect:/admin/manager-user";
	}

	@GetMapping("/delete")
	private String deleteUser(@RequestParam("userId") int userId) {
		userService.deleteUser(userId);
		return "redirect:/admin/manager-user";
	}
}
