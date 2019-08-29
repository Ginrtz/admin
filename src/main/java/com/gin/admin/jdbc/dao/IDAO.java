package com.gin.admin.jdbc.dao;

import java.sql.BatchUpdateException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.gin.admin.model.base.PageInfo;

public interface IDAO {
	/**
	 * 查询总数
	 *
	 * @param sql        count sql语句
	 * @param parameters 参数列表
	 * @return
	 */
	Number findCount(String sql, Object... parameters);

	/**
	 * 查询第一条数据
	 *
	 * @param sql        sql语句
	 * @param parameters 参数列表
	 * @return
	 */
	Map<String, Object> findFirst(String sql, Object... parameters);

	/**
	 * 查询第一条数据
	 *
	 * @param classOfItem 实体类class
	 * @param sql         sql语句
	 * @param parameters  参数列表
	 * @return
	 */
	<T> T findFirst(Class<T> classOfItem, String sql, Object... parameters);

	/**
	 * 查询列表
	 *
	 * @param sql        sql语句
	 * @param page       页码
	 * @param pageSize   页容量
	 * @param parameters 参数列表
	 * @return
	 */
	List<Map<String, Object>> findListPage(String sql, int page, int pageSize, Object... parameters);

	/**
	 * 查询列表
	 *
	 * @param sql        sql语句
	 * @param parameters 参数列表
	 * @return
	 */
	List<Map<String, Object>> findList(String sql, Object... parameters);

	/**
	 * 查询列表
	 *
	 * @param classOfItem 实体类class
	 * @param sql         sql语句
	 * @param page        页码
	 * @param pageSize    页容量
	 * @param parameters  参数列表
	 * @return
	 */
	<T> List<T> findListPage(Class<T> classOfItem, String sql, int page, int pageSize, Object... parameters);

	/**
	 * 查询列表
	 *
	 * @param classOfItem 实体类class
	 * @param sql         sql语句
	 * @param parameters  参数列表
	 * @return
	 */
	<T> List<T> findList(Class<T> classOfItem, String sql, Object... parameters);

	/**
	 * 查询分页
	 *
	 * @param pi         分页信息
	 * @param sql        sql语句
	 * @param parameters 参数列表
	 * @return
	 */
	PageInfo<Map<String, Object>> findPageInfo(PageInfo<?> pi, String sql, Object... sqlParameters);

	/**
	 * 查询分页
	 *
	 * @param pi          分页信息
	 * @param classOfItem 实体类class
	 * @param sql         sql语句
	 * @param parameters  参数列表
	 * @return
	 */
	<T> PageInfo<T> findPageInfo(PageInfo<?> pi, Class<T> classOfItem, String sql, Object... sqlParameters);

	/**
	 * 执行sql语句
	 *
	 * @param sql        sql语句
	 * @param parameters 参数列表
	 * @return 影响行数
	 */
	int execute(String sql, Object... parameters);

	/**
	 * 批量执行sql
	 *
	 * @param sqls sql语句数组
	 * @return 影响行数数组，和sql顺序一致
	 * @throws BatchUpdateException
	 */
	int[] batchExecute(String[] sqls) throws BatchUpdateException;

	/**
	 * 根据id获取实体
	 *
	 * @param classOfItem 实体类class
	 * @param id          主键
	 * @return
	 */
	<T> T find(Class<T> classOfItem, Object id);

	/**
	 * 保存实体，生成uuid主键
	 *
	 * @param o
	 */
	void save(Object o);

	/**
	 * 保存实体
	 *
	 * @param o          实体对象
	 * @param assignedId 是否指定主键<br>
	 *                   true：手动set主键或交由数据库生成 <br>
	 *                   false：生成uuid主键
	 */
	void save(Object o, boolean assignedId);

	/**
	 * 批量保存，生成uuid主键
	 *
	 * @param datas 实体list
	 * @throws BatchUpdateException
	 */
	void batchSave(List<?> datas) throws BatchUpdateException;

	/**
	 * 批量保存
	 *
	 * @param datas      实体list
	 * @param assignedId 是否指定主键<br>
	 *                   true：手动set主键或交由数据库生成 <br>
	 *                   false：生成uuid主键
	 * @throws BatchUpdateException
	 */
	void batchSave(List<?> datas, boolean assignedId) throws BatchUpdateException;

	/**
	 * 更新实体，包括null值
	 *
	 * @param o 实体对象
	 * @return
	 */
	int update(Object o);

	/**
	 * 更新实体
	 *
	 * @param o           实体对象
	 * @param ignoreNulls 是否忽略null值
	 * @return
	 */
	int update(Object o, boolean ignoreNulls);

	/**
	 * 删除实体
	 *
	 * @param o 实体对象
	 * @return
	 */
	<T> int delete(T o);

	/**
	 * 批量删除
	 *
	 * @param datas 实体list
	 * @throws BatchUpdateException
	 */
	void batchDelete(List<?> datas) throws BatchUpdateException;

	/**
	 * 查询返回rowset
	 *
	 * @param sql         sql语句
	 * @param currentPage 页码
	 * @param pageSize    页容量
	 * @param parameters  参数列表
	 * @return
	 */
	SqlRowSet findRowSet(String sql, int currentPage, int pageSize, Object... parameters);
}
