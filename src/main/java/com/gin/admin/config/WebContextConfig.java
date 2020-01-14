package com.gin.admin.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class WebContextConfig {

	@Resource
	private Environment env;

	@Resource
	private void configureViewGlobalVars(InternalResourceViewResolver resolver) {
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("SERVER_PUB_KEY", env.getProperty("auth.publicKey"));
		resolver.setAttributesMap(attributes);
	}
}
