package com.gin.admin.cg.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.gin.admin.cg.model.ModelInfo;
import com.gin.admin.util.SpringUtils;
import com.gin.admin.util.StringUtils;

/**
 * 获取数据表对应实体类元信息工具
 *
 * @author o1760 2019年10月31日
 */
public class ModelUtil {
	private static JdbcTemplate jdbcTemplate = SpringUtils.getBean(JdbcTemplate.class);
	private static Logger log = LoggerFactory.getLogger(ModelUtil.class);
	public static List<String> treeNames = Arrays.asList("PARENT_ID", "TREE_SORT");

	/**
	 * 获取实体类信息
	 *
	 * @param packageName 生成实体类的包名
	 * @param tableName   数据表名称
	 * @return 实体类元信息
	 * @author o1760 2019年10月31日
	 */
	public static ModelInfo getModelInfo(final String packageName, final String tableName, boolean isTree) {
		try {
			ModelInfo modelInfo = new ModelInfo();
			modelInfo.setPackageName(packageName);
			modelInfo.setClassName(StringUtils.capitalize(StringUtils.toJavaName(tableName)));
			Connection conn = jdbcTemplate.getDataSource().getConnection();
			DatabaseMetaData dbMetaData = conn.getMetaData();
			ResultSet tables = dbMetaData.getTables(conn.getCatalog(), null, tableName, new String[] { "TABLE" });
			while (tables.next()) {
				modelInfo.setRemark(tables.getString("REMARKS"));// 表注释
			}
			ResultSet columns = dbMetaData.getColumns(conn.getCatalog(), null, tableName.toUpperCase(), "%");
			Map<String, String> remarkMap = new HashMap<>();
			// 获取字段注释
			while (columns.next()) {
				String colName = columns.getString("COLUMN_NAME").toUpperCase();
				if (isTree && treeNames.contains(colName)) {
					continue;
				}
				String remarks = columns.getString("REMARKS");
				if (StringUtils.isEmpty(remarks)) {
					remarks = colName;
				}
				remarkMap.put(colName, remarks);
			}
			String sql = "select * from " + tableName + " limit 1";
			// 获取字段对应java类
			return jdbcTemplate.queryForObject(sql, (RowMapper<ModelInfo>) (rs, rowNum) -> {
				List<String> names = new ArrayList<>();
				List<String> capitalizedNames = new ArrayList<>();
				List<String> classes = new ArrayList<>();
				List<String> remarks = new ArrayList<>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				for (int i = 1; i <= columnCount; i++) {
					String columnName = metaData.getColumnName(i);
					if (isTree && treeNames.contains(columnName.toUpperCase())) {
						continue;
					}
					String columnClassName = metaData.getColumnClassName(i);
					String fieldName = StringUtils.toJavaName(columnName);

					names.add(fieldName);
					capitalizedNames.add(StringUtils.capitalize(fieldName));
					classes.add(columnClassName.replace("java.lang.", ""));
					remarks.add(remarkMap.get(columnName.toUpperCase()));
				}
				modelInfo.setFieldNames(names);
				modelInfo.setCapitalizedNames(capitalizedNames);
				modelInfo.setFieldClasses(classes);
				modelInfo.setRemarks(remarks);
				return modelInfo;
			});
		} catch (Exception e) {
			log.error("获取表" + tableName + "的元信息出错", e);
			return null;
		}
	}
}
