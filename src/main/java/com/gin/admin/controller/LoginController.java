package com.gin.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gin.admin.dao.BaseDao;
import com.gin.admin.model.User;
import com.gin.admin.model.base.JsonResult;
import com.gin.admin.util.JwtUtil;
import com.gin.admin.util.RSAUtils;
import com.gin.admin.util.StringUtils;

@Controller
public class LoginController {
	@Autowired
	private BaseDao dao;
	@Autowired
	private JwtUtil jwtUtil;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login/login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult login(User user) {
		JsonResult result = new JsonResult();
		if (StringUtils.isEmpty(user.getUserName()) || StringUtils.isEmpty(user.getPassword())) {
			result.setCode(JsonResult.CODE_VERIFY_ERROR);
			return result;
		}
		try {
			// 用私钥解密
			user.setPassword(new String(RSAUtils.decryptByPrivateKey(user.getPassword(), jwtUtil.getPrivateKey())));
		} catch (Exception e) {
			result.setCode(JsonResult.CODE_VERIFY_ERROR);
			return result;
		}
		User u = dao.find(User.class, "select * from user where user_name=? and password=?", user.getUserName(), user.getPassword());
		if (u == null) {
			result.setCode(JsonResult.CODE_VERIFY_ERROR);
			return result;
		}
		String token = jwtUtil.getToken(u);
		result.put("token", token);
		return result;
	}

	@RequestMapping("/logout")
	public JsonResult logout(HttpServletRequest request) {
		jwtUtil.removeToken(jwtUtil.getCurrToken(request));
		return JsonResult.success;
	}
}
