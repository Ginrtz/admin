package com.gin.admin.util;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {
	public static boolean isAjax(HttpServletRequest request) {
		return request.getHeader("x-requested-with") != null
				&& request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest");
	}
}
