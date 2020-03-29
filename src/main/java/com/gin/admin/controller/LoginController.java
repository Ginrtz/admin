package com.gin.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gin.admin.dao.JdbcDao;
import com.gin.admin.jwt.JwtManager;
import com.gin.admin.model.User;
import com.gin.admin.model.base.JsonResult;
import com.gin.admin.util.RSAUtils;
import com.gin.admin.util.StringUtils;

@Controller
public class LoginController {
	@Autowired
	private JdbcDao dao;
	@Autowired
	private JwtManager jwtUtil;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login/login";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return "main/index";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult login(HttpServletRequest request, User user) {
		JsonResult result = new JsonResult();
		if (StringUtils.isEmpty(user.getUserName()) || StringUtils.isEmpty(user.getPassword())) {
			result.setCode(JsonResult.CODE_VERIFY_ERROR);
			result.setMessage("用户名或密码错误");
			return result;
		}
		try {
			// 用私钥解密
			user.setPassword(new String(RSAUtils.decryptByPrivateKey(user.getPassword(), jwtUtil.getPrivateKey())));
		} catch (Exception e) {
			result.setCode(JsonResult.CODE_VERIFY_ERROR);
			result.setMessage("用户名或密码错误");
			return result;
		}
		User u = dao.find(User.class, "select * from user where user_name=? and password=?", user.getUserName(), user.getPassword());
		if (u == null) {
			result.setCode(JsonResult.CODE_VERIFY_ERROR);
			result.setMessage("用户名或密码错误");
			return result;
		}
		String token = jwtUtil.getToken(u, request.getSession().getId());
		result.put("token", token);
		result.put("user", u);
		return result;
	}

	@RequestMapping("/logout")
	public JsonResult logout(HttpServletRequest request) {
		jwtUtil.removeToken(request);
		return JsonResult.success;
	}
}
