package com.gin.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gin.admin.model.base.ResResult;

@Controller
public class MyErrorController implements ErrorController {

	@Value("${error.debug:false}")
	public boolean debug;

	@Override
	public String getErrorPath() {
		return "/error";
	}

	@RequestMapping("/error")
	@ResponseBody
	public ResResult handleError(HttpServletRequest request) {
		ResResult result = new ResResult();
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		result.setCode(statusCode);
		if (debug) {
			Exception ex = (Exception)request.getAttribute("org.springframework.web.servlet.DispatcherServlet.EXCEPTION");
			result.setMessage(ex.getMessage());
		} else {
			result.setMessage("接口异常");
		}
		return result;
	}

}
