package com.gin.admin.hibernate.enums;

public enum SortDirection {
	/** 升序 */
	asc,
	/** 降序 */
	desc;

	public static SortDirection toEnum(String s) {
		if ("desc".equalsIgnoreCase(s)) {
			return desc;
		}
		return asc;
	}
}
