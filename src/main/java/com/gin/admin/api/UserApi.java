package com.gin.admin.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gin.admin.constants.Constants;
import com.gin.admin.dao.BaseDao;
import com.gin.admin.model.User;
import com.gin.admin.model.base.ResResult;
import com.gin.admin.util.JwtUtil;
import com.gin.admin.util.RSAUtils;
import com.gin.admin.util.SqlUtils;
import com.gin.admin.util.StringUtils;

@RestController
@RequestMapping("/user")
public class UserApi {
	@Autowired
	private BaseDao dao;
	@Autowired
	private JwtUtil jwtUtil;

	@RequestMapping("/login")
	public ResResult login(User user) {
		ResResult result = new ResResult();
		if (StringUtils.isEmpty(user.getUserName())) {
			result.setCode(ResResult.CODE_PARAMETER_ERROR);
			result.setMessage("parameter userName is empty");
			return result;
		}
		if (StringUtils.isEmpty(user.getPassword())) {
			result.setCode(ResResult.CODE_PARAMETER_ERROR);
			result.setMessage("parameter password is empty");
			return result;
		}
		try {
			user.setPassword(new String(RSAUtils.decryptByPrivateKey(user.getPassword(), Constants.AUTH_PRI_KEY)));
		} catch (Exception e) {
			result.setCode(ResResult.CODE_VERIFY_ERROR);
			return result;
		}
		User u = dao.find(User.class, "select * from user where user_name=? and password=?", user.getUserName(),
				user.getPassword());
		if (u == null) {
			result.setCode(ResResult.CODE_VERIFY_ERROR);
			return result;
		}
		String token = jwtUtil.getToken(u);
		Map<String, Object> data = new HashMap<>();
		data.put("token", token);
		result.setData(data);
		return result;
	}

	@RequestMapping("/info")
	public ResResult getUserInfo(HttpServletRequest request) {
		User user = jwtUtil.getCurrUser(request);
		ResResult result = new ResResult();
		result.setEntity(user);
		return result;
	}

	@RequestMapping("/logout")
	public ResResult logout(HttpServletRequest request) {
		jwtUtil.removeToken(jwtUtil.getCurrToken(request));
		return ResResult.success;
	}

	@RequestMapping("/getkey")
	public ResResult getKey() {
		ResResult result = new ResResult();
		result.put("key", Constants.AUTH_PUB_KEY);
		return result;
	}

	@RequestMapping("/list")
	public ResResult getList(User user, Integer page, Integer limit) {
		ResResult result = new ResResult();
		List<Object> parameters = new ArrayList<Object>();
		String sql = SqlUtils.createSelectSql(user, parameters);
		List<User> list = dao.findObjForListPage(User.class, sql, page, limit, parameters.toArray());
		Long total = dao.countForJdbc(sql, parameters.toArray());
		Map<String, Object> items = new HashMap<>();
		items.put("items", list);
		items.put("total", total);
		result.setData(items);
		return result;
	}
}
