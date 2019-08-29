package com.gin.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "")
public class UserController {
	@RequestMapping(value = "")
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/user")
	@ResponseBody
	public Object user() {
		return null;
	}
}
