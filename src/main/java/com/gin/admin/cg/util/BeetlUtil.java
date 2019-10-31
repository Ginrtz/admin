package com.gin.admin.cg.util;

import java.io.IOException;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;

/**
 * beetl工具类
 *
 * @author o1760 2019年10月31日
 */
public class BeetlUtil {
	/**
	 * 读取beetl模板，在resources/template/ftl/目录下
	 *
	 * @param templateName 模板名称，带后缀名
	 * @return beetl模板
	 * @throws IOException 模板文件读取失败
	 * @author o1760 2019年10月31日
	 */
	public static Template getTemplate(String templateName) throws IOException {
		ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("template/ftl/");
		Configuration cfg = Configuration.defaultConfiguration();
		GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
		return gt.getTemplate(templateName);
	}
}
