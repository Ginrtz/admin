package com.gin.admin.hibernate.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.persistence.Transient;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gin.admin.hibernate.model.CriteriaQuery;
import com.gin.admin.hibernate.model.QueryParam;
import com.gin.admin.hibernate.model.SubQuery;
import com.gin.admin.util.StringUtils;

public class QueryUtil {

	static Logger log = LoggerFactory.getLogger(QueryUtil.class);

	/**
	 * 时间查询符号
	 */
	private static final String END = "_end";
	private static final String BEGIN = "_begin";

	private static final ThreadLocal<SimpleDateFormat> local = new ThreadLocal<>();

	private static SimpleDateFormat getTime() {
		SimpleDateFormat time = local.get();
		if (time == null) {
			time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			local.set(time);
		}
		return time;
	}

	/**
	 * 自动生成查询条件HQL 模糊查询 不带有日期组合
	 *
	 * @param cq
	 * @param searchObj
	 * @throws Exception
	 */
	public static void installBeanCondition(CriteriaQuery<?> cq, Object searchObj) {
		installBeanCondition(cq, searchObj, null);

	}

	/**
	 * 自动生成查询条件HQL（扩展区间查询功能）
	 *
	 * @param cq
	 * @param searchObj
	 * @param parameterMap request参数集合，封装了所有查询信息
	 */
	public static void installBeanCondition(CriteriaQuery<?> cq, Object searchObj, Map<String, String[]> parameterMap) {
		PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(searchObj);
		String name, type;
		for (PropertyDescriptor origDescriptor : origDescriptors) {
			name = origDescriptor.getName();
			type = origDescriptor.getPropertyType().toString();
			try {
				if (judgedIsUselessField(name) || !PropertyUtils.isReadable(searchObj, name)) {
					continue;
				}
				Field field = getField(searchObj.getClass(), name);
				if (field != null && field.isAnnotationPresent(Transient.class)) {
					continue;
				}
				// 添加 判断是否有区间值
				String beginValue = null;
				String endValue = null;
				if (parameterMap != null && parameterMap.containsKey(name + BEGIN)) {
					beginValue = parameterMap.get(name + BEGIN)[0].trim();
				}
				if (parameterMap != null && parameterMap.containsKey(name + END)) {
					endValue = parameterMap.get(name + END)[0].trim();
				}

				Object value = PropertyUtils.getSimpleProperty(searchObj, name);
				if (type.contains("class java.lang") || type.contains("class java.math")) {
					if (value != null && !value.equals("")) {
						if (null != value && value.toString().startsWith(",") && value.toString().endsWith(",")) {
							String[] vals = value.toString().replace(",,", ",").split(",");
							for (String val : vals) {
								if (StringUtils.isNotEmpty(val)) {
									cq.eq(name, val);
								}
							}
						} else {
							if (cq.getFuzzyFields().contains(name + ",")) {
								value = "%" + value + "%";
								cq.like(name, value);
							} else {
								cq.eq(name, value);
							}
						}

					} else if (parameterMap != null) {
						Object beginValue_ = null, endValue_ = null;
						if ("class java.lang.Integer".equals(type)) {
							if (!"".equals(beginValue) && null != beginValue) {
								beginValue_ = Integer.parseInt(beginValue);
							}
							if (!"".equals(endValue) && null != endValue) {
								endValue_ = Integer.parseInt(endValue);
							}
						} else if ("class java.math.BigDecimal".equals(type)) {
							if (!"".equals(beginValue) && null != beginValue) {
								beginValue_ = new BigDecimal(beginValue);
							}
							if (!"".equals(endValue) && null != endValue) {
								endValue_ = new BigDecimal(endValue);
							}
						} else if ("class java.lang.Short".equals(type)) {
							if (!"".equals(beginValue) && null != beginValue) {
								beginValue_ = Short.parseShort(beginValue);
							}
							if (!"".equals(endValue) && null != endValue) {
								endValue_ = Short.parseShort(endValue);
							}
						} else if ("class java.lang.Long".equals(type)) {
							if (!"".equals(beginValue) && null != beginValue) {
								beginValue_ = Long.parseLong(beginValue);
							}
							if (!"".equals(endValue) && null != endValue) {
								endValue_ = Long.parseLong(endValue);
							}
						} else if ("class java.lang.Float".equals(type)) {
							if (!"".equals(beginValue) && null != beginValue) {
								beginValue_ = Float.parseFloat(beginValue);
							}
							if (!"".equals(endValue) && null != endValue) {
								endValue_ = Float.parseFloat(endValue);
							}

						} else if ("class java.lang.Double".equals(type)) {
							if (!"".equals(beginValue) && null != beginValue) {
								beginValue_ = Double.parseDouble(beginValue);
							}
							if (!"".equals(endValue) && null != endValue) {
								endValue_ = Double.parseDouble(endValue);
							}

						} else {
							beginValue_ = beginValue;
							endValue_ = endValue;
						}
						cq.ge(name, beginValue_);
						cq.le(name, endValue_);
					}

					// for：查询拼装的替换
				} else if ("class java.util.Date".equals(type)) {
					if (StringUtils.isNotEmpty(beginValue)) {
						if (beginValue.length() == 19) {
							cq.ge(name, getTime().parse(beginValue));
						} else if (beginValue.length() == 10) {
							cq.ge(name, getTime().parse(beginValue + " 00:00:00"));
						}
					}
					if (StringUtils.isNotEmpty(endValue)) {
						if (endValue.length() == 19) {
							cq.le(name, getTime().parse(endValue));
						} else if (endValue.length() == 10) {
							cq.le(name, getTime().parse(endValue + " 23:59:59"));
						}
					}
					if (StringUtils.isNotEmpty(value)) {
						cq.eq(name, value);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static boolean judgedIsUselessField(String name) {
		return "class".equals(name) || "ids".equals(name) || "page".equals(name) || "rows".equals(name)
				|| "sort".equals(name) || "order".equals(name);
	}

	public static <T> Predicate createQueryCondition(CriteriaBuilder builder, Root<T> root, QueryParam param) {
		Object obj = param.getObj();
		Class<?> fieldClass = param.getFieldClass();
		String field = param.getField();
		Path<Object> path = null;
		String[] names = null;
		if (field.indexOf(".") > 0) {
			names = field.split("\\.");
			Join<Object, Object> join = root.join(names[0], JoinType.LEFT);
			path = join.get(names[1]);
		} else {
			path = root.get(field);
		}
		switch (param.getOperator()) {
		case EQUAL:
			return builder.equal(path, obj);
		case NOT_EQUAL:
			return builder.notEqual(path, obj);
		case GREATER_THAN:
			if (fieldClass.equals(Date.class)) {
				return builder.greaterThan(path.as(Date.class), (Date) obj);
			} else {
				return builder.gt(path.as(Number.class), (Number) obj);
			}
		case GREATER_THAN_OR_EQUAL:
			if (fieldClass.equals(Date.class)) {
				return builder.greaterThanOrEqualTo(path.as(Date.class), (Date) obj);
			} else {
				return builder.ge(path.as(Number.class), (Number) obj);
			}
		case LESS_THAN:
			if (fieldClass.equals(Date.class)) {
				return builder.lessThan(path.as(Date.class), (Date) obj);
			} else {
				return builder.lt(path.as(Number.class), (Number) obj);
			}
		case LESS_THAN_OR_EQUAL:
			if (fieldClass.equals(Date.class)) {
				return builder.lessThanOrEqualTo(path.as(Date.class), (Date) obj);
			} else {
				return builder.le(path.as(Number.class), (Number) obj);
			}
		case IN:
			In<Object> in = builder.in(path);
			Object[] objs = (Object[]) obj;
			if (objs.length > 0) {
				for (Object o : objs) {
					in.value(o);
				}
			}
			return in;
		case NOT_IN:
			in = builder.in(path);
			objs = (Object[]) obj;
			if (objs.length > 0) {
				for (Object o : objs) {
					in.value(o);
				}
			}
			return builder.not(in);
		case LIKE:
			return builder.like(path.as(String.class), "%" + obj + "%");
		case IS_NULL:
			return builder.isNull(path);
		case IS_NOT_NULL:
			return builder.isNotNull(path);
		default:
			break;
		}
		return builder.conjunction();
	}

	public static Predicate createSubQueryCondition(CriteriaBuilder builder, AbstractQuery<?> query, Root<?> root,
			SubQuery subQuery) {
		String projection = subQuery.getQuery().getProjection();
		String property = subQuery.getProperty();
		Class<?> entityClass = subQuery.getQuery().getEntityClass();
		Class<?> projectionClass = null;
		Predicate predicate = builder.conjunction();
		try {
			projectionClass = getFieldType(entityClass, projection);
		} catch (NoSuchFieldException | SecurityException e) {
			log.error(entityClass.getName() + "类不包含名称为" + projection + "的属性", e);
			predicate = builder.disjunction();
		}
		if (projectionClass != null) {
			Subquery<?> subquery = query.subquery(projectionClass);
			Root<?> from = subquery.from(entityClass);
			Predicate restrictions = builder.conjunction();
			for (QueryParam param : subQuery.getQuery().getQueryParams()) {
				restrictions = builder.and(restrictions, QueryUtil.createQueryCondition(builder, from, param));
			}

			for (SubQuery sq : subQuery.getQuery().getSubQuerys()) {
				restrictions = builder.and(restrictions, createSubQueryCondition(builder, subquery, from, sq));
			}

			String[] names = null;
			if (projection.indexOf(".") > 0) {
				names = projection.split("\\.");
				Join<Object, Object> join = from.join(names[0], JoinType.LEFT);
				subquery.select(join.get(names[1])).where(restrictions);
			} else {
				subquery.select(from.get(projection)).where(restrictions);
			}

			Path<Object> path = null;
			if (property.indexOf(".") > 0) {
				names = property.split("\\.");
				Join<Object, Object> join = root.join(names[0], JoinType.LEFT);
				path = join.get(names[1]);
			} else {
				path = root.get(property);
			}
			switch (subQuery.getOpt()) {
			case EQUAL:
				predicate = builder.equal(path, subquery);
				break;
			case NOT_EQUAL:
				predicate = builder.notEqual(path, subquery);
				break;
			case GREATER_THAN:
				if (projectionClass.equals(Date.class)) {
					predicate = builder.greaterThan(path.as(Date.class), subquery.as(Date.class));
				} else {
					predicate = builder.gt(path.as(Number.class), subquery.as(Number.class));
				}
				break;
			case GREATER_THAN_OR_EQUAL:
				if (projectionClass.equals(Date.class)) {
					predicate = builder.greaterThanOrEqualTo(path.as(Date.class), subquery.as(Date.class));
				} else {
					predicate = builder.ge(path.as(Number.class), subquery.as(Number.class));
				}
				break;
			case LESS_THAN:
				if (projectionClass.equals(Date.class)) {
					predicate = builder.lessThan(path.as(Date.class), subquery.as(Date.class));
				} else {
					predicate = builder.lt(path.as(Number.class), subquery.as(Number.class));
				}
				break;
			case LESS_THAN_OR_EQUAL:
				if (projectionClass.equals(Date.class)) {
					predicate = builder.lessThanOrEqualTo(path.as(Date.class), subquery.as(Date.class));
				} else {
					predicate = builder.le(path.as(Number.class), subquery.as(Number.class));
				}
				break;
			case IN:
				predicate = path.in(subquery);
				break;
			case NOT_IN:
				predicate = builder.not(path.in(subquery));
				break;
			case EXISTS:
				predicate = builder.exists(subquery);
				break;
			case NOT_EXISTS:
				predicate = builder.not(builder.exists(subquery));
				break;
			default:
				break;
			}
		}
		return predicate;
	}

	private static Class<?> getFieldType(Class<?> entityClass, String fieldName)
			throws NoSuchFieldException, SecurityException {
		int index = fieldName.indexOf(".");
		if (index > 0) {
			String name1 = fieldName.substring(0, index);
			String name2 = fieldName.substring(index + 1);
			return getFieldType(getField(entityClass, name1).getType(), name2);
		} else {
			return getField(entityClass, fieldName).getType();
		}
	}

	private static Field getField(Class<?> clazz, String fieldName) {
		Field field = null;
		while (clazz != Object.class) {
			try {
				field = clazz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException | SecurityException e) {
			}
			if (field == null) {
				clazz = clazz.getSuperclass();
			} else {
				return field;
			}
		}
		return null;
	}
}
