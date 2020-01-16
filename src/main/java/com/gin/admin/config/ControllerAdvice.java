package com.gin.admin.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * 注入全局变量
 *
 * @author o1760 2020年1月16日
 *
 */
@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

	/**
	 * 站点上下文
	 *
	 * @author o1760 2020年1月16日
	 */
	@ModelAttribute(name = "ctx")
	public String getContextPath(HttpServletRequest request) {
		return request.getContextPath();
	}
	/**
	 * 静态资源上下文
	 *
	 * @author o1760 2020年1月16日
	 */
	@ModelAttribute(name = "ctxStatic")
	public String getStaticContextPath(HttpServletRequest request) {
		return request.getContextPath() + "/static";
	}
}
