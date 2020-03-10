package com.gin.admin.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.gin.admin.jwt.JwtManager;
import com.gin.admin.model.base.JsonResult;
import com.gin.admin.util.HttpUtil;
import com.gin.admin.util.JsonUtil;

/**
 * 请求鉴权拦截器
 *
 * @author o1760
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {
	@Autowired
	private JwtManager jwtUtil;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		// 验证token
		JsonResult result = jwtUtil.checkToken(request);
		if (result.getCode() != JsonResult.CODE_SUCCESS) {
			if (HttpUtil.isAjax(request)) {
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json; charset=utf-8");
				PrintWriter writer = response.getWriter();
				writer.write(JsonUtil.toJsonString(result));
				writer.flush();
				writer.close();
			} else {
				response.sendRedirect(request.getContextPath() + "/login");
			}
			return false;
		}
		request.setAttribute("currUserName", result.get("username"));
		return true;
	}
}
