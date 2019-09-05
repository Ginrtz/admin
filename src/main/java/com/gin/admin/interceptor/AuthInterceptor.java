package com.gin.admin.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.fastjson.JSON;
import com.gin.admin.model.base.ResponseResult;
import com.gin.admin.util.JwtUtil;

/**
 * 登录拦截器
 *
 * @author o1760
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		String token = request.getHeader(JwtUtil.TOKEN_KEY);
		// 验证token
		ResponseResult result = jwtUtil.checkToken(token);
		if (result.getCode() != ResponseResult.CODE_SUCCESS) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.write(JSON.toJSONString(result));
			writer.flush();
			writer.close();
			return false;
		}
		request.setAttribute("currUserName", result.get("username"));
		return true;
	}
}
