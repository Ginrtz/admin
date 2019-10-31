package com.gin.admin.cg.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.beetl.core.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gin.admin.cg.model.ModelInfo;

/**
 * 代码生成工具类
 *
 * @author o1760 2019年10月31日
 */
public class CgUtil {

	private static Logger log = LoggerFactory.getLogger(CgUtil.class);

	/**
	 * 批量生成model实体类,不适用树形表
	 *
	 * @param packageName 包名
	 * @param tableNames  数据表名数组
	 * @author o1760 2019年10月31日
	 */
	public static void genModel(final String packageName, String[] tableNames) {
		for (String tableName : tableNames) {
			genModel(packageName, tableName, false);
		}
	}

	/**
	 * 生成model实体类
	 *
	 * @param packageName 包名
	 * @param tableName   数据表名
	 * @author o1760 2019年10月30日
	 */
	public static void genModel(final String packageName, final String tableName, boolean isTree) {
		ModelInfo modelInfo = ModelUtil.getModelInfo(packageName, tableName, isTree);
		try {
			Template template = BeetlUtil.getTemplate(isTree ? "tree_model.btl" : "model.btl");
			template.binding("info", modelInfo);
			FileOutputStream fos = new FileOutputStream(
					new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "codes" + File.separator + modelInfo.getClassName() + ".java"));
			template.renderTo(fos);
			log.info(modelInfo.getRemark() + "表实体类生成完成");
		} catch (IOException e) {
			log.error(modelInfo.getRemark() + "表实体类生成异常", e);
		}
	}
}
