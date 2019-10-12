package com.gin.admin.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gin.admin.model.User;
import com.gin.admin.model.base.ResponseResult;
import com.gin.admin.util.JwtUtil;
import com.gin.admin.util.StringUtils;
import com.gin.nicedao.NiceDao;

@RestController
@RequestMapping("/user")
public class UserApi {
	@Autowired
	private NiceDao dao;
	@Autowired
	private JwtUtil jwtUtil;

	@RequestMapping("/login")
	public ResponseResult login(@RequestBody User user) {
		if (StringUtils.isEmpty(user.getUserName()) || StringUtils.isEmpty(user.getPassword())) {
			return null;
		}
		User u = dao.find(User.class, "select * from user where user_name=? and password=?", user.getUserName(),
				user.getPassword());
		ResponseResult result = new ResponseResult();
		if (u == null) {
			result.setCode(ResponseResult.CODE_VERIFY_ERROR);
			return result;
		}
		result.setCode(ResponseResult.CODE_SUCCESS);
		String token = jwtUtil.getToken(user);
		Map<String, Object> data = new HashMap<>();
		data.put("token", token);
		result.setData(data);
		return result;
	}

	@RequestMapping("/info")
	public ResponseResult getUserInfo() {
		ResponseResult result = new ResponseResult();
		Map<String, Object> data = new HashMap<>();
		data.put("roles", "admin");
		data.put("introduction", "I am a super administrator");
		data.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
		data.put("name", "管理员");
		result.setData(data);
		return result;
	}

	@RequestMapping("/logout")
	public ResponseResult logout(HttpServletRequest request) {
		jwtUtil.removeToken(jwtUtil.getCurrToken(request));
		return ResponseResult.success;
	}
}
