package com.gin.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class MyErrorController {

	@RequestMapping("/400")
	public String toPage400() {
		return "error/error-400";
	}

	@RequestMapping("/404")
	public String toPage404() {
		return "error/error-404";
	}

	@RequestMapping("/500")
	public String toPage500() {
		return "error/error-500";
	}
}
