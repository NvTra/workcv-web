package com.tranv.workcv.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;

public class CustomErrorController{
	@RequestMapping("/error")
	public String handleError(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode != null) {
			if (statusCode == HttpStatus.NOT_FOUND.value()) {
				return "errors/error-404";
			}
		}
		return "error";
	}

	public String getErrorPath() {
		return "/error";
	}
}
