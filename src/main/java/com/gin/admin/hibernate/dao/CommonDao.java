package com.gin.admin.hibernate.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gin.admin.hibernate.enums.Operator;
import com.gin.admin.hibernate.enums.SortDirection;
import com.gin.admin.hibernate.model.CriteriaQuery;
import com.gin.admin.hibernate.model.QueryParam;
import com.gin.admin.hibernate.model.SubQuery;
import com.gin.admin.hibernate.util.QueryUtil;

/**
 * hibernate dao
 * 
 * @author o1760
 *
 */
@Repository
public class CommonDao {
	private static final Logger logger = LoggerFactory.getLogger(CommonDao.class);

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	public Session getSession() {
		return entityManagerFactory.unwrap(SessionFactory.class).getCurrentSession();
	}
	
	/**
	 * 根据主键获取实体
	 */

	public <T> T getEntity(Class<T> entityName, Serializable id) {
		T t = getSession().get(entityName, id);
		return t;
	}

	/**
	 * 根据实体属性获取第一条记录 key1,value1,key2,value2...
	 */

	public <T> T getEntityByProperties(Class<T> entityClass, Object... propertyNamesAndValues) {
		List<T> list = getListByProperties(entityClass, propertyNamesAndValues);
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 按属性查找对象列表 key1,value1,key2,value2...
	 */
	public <T> List<T> getListByProperties(Class<T> entityClass, Object... propertyNamesAndValues) {
		CriteriaBuilder builder = getSession().getCriteriaBuilder();
		javax.persistence.criteria.CriteriaQuery<T> dataQuery = builder.createQuery(entityClass);
		Root<T> root = dataQuery.from(entityClass);
		dataQuery.select(root);
		Predicate restrictions = builder.conjunction();
		if (propertyNamesAndValues != null && propertyNamesAndValues.length > 1) {
			for (int i = 1; i < propertyNamesAndValues.length; i += 2) {
				String name = (String) propertyNamesAndValues[i - 1];
				Object value = propertyNamesAndValues[i];
				restrictions = builder.and(restrictions,
						QueryUtil.createQueryCondition(builder, root, new QueryParam(name, value, Operator.EQUAL)));
			}
		}
		dataQuery.where(restrictions);
		return getSession().createQuery(dataQuery).getResultList();
	}

	/**
	 * 根据传入的实体持久化对象
	 */

	public <T> Serializable save(T entity) {
		try {
			Serializable id = getSession().save(entity);
			if (logger.isDebugEnabled()) {
				logger.debug("保存实体成功," + entity.getClass().getName());
			}
			return id;
		} catch (RuntimeException e) {
			logger.error("保存实体异常", e);
			throw e;
		}

	}

	/**
	 * 批量保存数据
	 */

	public <T> void batchSave(List<T> entitys) {
		for (int i = 0; i < entitys.size(); i++) {
			getSession().save(entitys.get(i));
			if (i % 1000 == 0) {
				getSession().flush();
				getSession().clear();
			}
		}
		// 最后页面的数据，进行提交手工清理
		getSession().flush();
		getSession().clear();
	}

	/**
	 * 根据传入的实体添加或更新对象
	 */

	public <T> void saveOrUpdate(T entity) {
		try {
			getSession().saveOrUpdate(entity);
			if (logger.isDebugEnabled()) {
				logger.debug("添加或更新成功," + entity.getClass().getName());
			}
		} catch (RuntimeException e) {
			logger.error("添加或更新异常", e);
			throw e;
		}
	}

	/**
	 * 根据传入的实体删除对象
	 */

	public <T> void delete(T entity) {
		try {
			getSession().delete(getSession().merge(entity));
			if (logger.isDebugEnabled()) {
				logger.debug("删除成功," + entity.getClass().getName());
			}
		} catch (RuntimeException e) {
			logger.error("删除异常", e);
			throw e;
		}
	}

	/**
	 * 根据主键删除指定的实体
	 */

	public <T> void deleteEntityById(Class<T> clazz, Serializable id) {
		delete(getEntity(clazz, id));
	}

	/**
	 * 删除全部的实体
	 */

	public <T> void deleteAll(Collection<T> entitys) {
		for (Object entity : entitys) {
			getSession().delete(entity);
		}
	}

	/**
	 * 按类型加载全部数据
	 */
	public <T> List<T> loadAll(final Class<T> entityClass) {
		CriteriaBuilder builder = getSession().getCriteriaBuilder();
		javax.persistence.criteria.CriteriaQuery<T> query = builder.createQuery(entityClass);
		query.from(entityClass);
		query.where(builder.conjunction());
		return getSession().createQuery(query).getResultList();
	}

	/**
	 * 查询总数
	 */
	public <T> Integer getCountByCriteriaQuery(final CriteriaQuery<T> cq) {
		CriteriaBuilder builder = getSession().getCriteriaBuilder();
		javax.persistence.criteria.CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<T> countRoot = countQuery.from(cq.getEntityClass());
		Predicate restrictions = builder.conjunction();
		for (QueryParam param : cq.getQueryParams()) {
			restrictions = builder.and(restrictions, QueryUtil.createQueryCondition(builder, countRoot, param));
		}
		for (SubQuery subQuery : cq.getSubQuerys()) {
			restrictions = builder.and(restrictions,
					QueryUtil.createSubQueryCondition(builder, countQuery, countRoot, subQuery));
		}
		countQuery.where(restrictions);
		countQuery.select(builder.count(countRoot));
		Long total = getSession().createQuery(countQuery).getSingleResult();
		return total.intValue();
	}

	/**
	 * 根据CriteriaQuery获取List
	 */
	public <T> List<T> getListByCriteriaQuery(final CriteriaQuery<T> cq, Boolean ispage) {
		CriteriaBuilder builder = getSession().getCriteriaBuilder();
		javax.persistence.criteria.CriteriaQuery<T> dataQuery = builder.createQuery(cq.getEntityClass());
		Root<T> root = dataQuery.from(cq.getEntityClass());
		Predicate restrictions = builder.conjunction();
		for (QueryParam param : cq.getQueryParams()) {
			restrictions = builder.and(restrictions, QueryUtil.createQueryCondition(builder, root, param));
		}
		for (SubQuery subQuery : cq.getSubQuerys()) {
			restrictions = builder.and(restrictions,
					QueryUtil.createSubQueryCondition(builder, dataQuery, root, subQuery));
		}
		Map<String, SortDirection> orderMap = cq.getOrderMap();
		if (!orderMap.isEmpty()) {
			Iterator<Entry<String, SortDirection>> iterator = orderMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, SortDirection> orderEntry = iterator.next();
				if (orderEntry.getValue().equals(SortDirection.asc)) {
					dataQuery.orderBy(builder.asc(root.get(orderEntry.getKey())));
				} else {
					dataQuery.orderBy(builder.desc(root.get(orderEntry.getKey())));
				}
			}
		}
		dataQuery.where(restrictions);
		Query<T> pageQuery = getSession().createQuery(dataQuery);
		if (ispage) {
			pageQuery.setFirstResult((cq.getCurPage() - 1) * cq.getPageSize());
			pageQuery.setMaxResults(cq.getPageSize());
		}
		List<T> resultList = pageQuery.getResultList();
		return resultList;

	}

}
