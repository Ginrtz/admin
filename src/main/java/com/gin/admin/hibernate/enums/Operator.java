package com.gin.admin.hibernate.enums;

public enum Operator {
	/** 相等 */
	EQUAL,
	/** 不相等 */
	NOT_EQUAL,
	/** 大于 */
	GREATER_THAN,
	/** 大于等于 */
	GREATER_THAN_OR_EQUAL,
	/** 小于 */
	LESS_THAN,
	/** 小于等于 */
	LESS_THAN_OR_EQUAL,
	/** 包含在内 */
	IN,
	/** 不包含在内 */
	NOT_IN,
	/** 为空 */
	IS_NULL,
	/** 不为空 */
	IS_NOT_NULL,
	/** 模糊 */
	LIKE,
	/** 存在 */
	EXISTS,
	/** 不存在 */
	NOT_EXISTS;
}
