package com.gin.admin.jdbc.dao.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.springframework.beans.factory.annotation.Autowired;

import com.gin.admin.jdbc.dao.BaseDao;

public class DaoProxy implements InvocationHandler {
	@Autowired
	private BaseDao baseDao;

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Method[] methods = BaseDao.class.getDeclaredMethods();
		for (Method me : methods) {
			if (isMethodSame(method, me)) {
				return me.invoke(baseDao, args);
			}
		}
		return null;
	}

	private boolean isMethodSame(Method m1, Method m2) {
		if (!m1.getName().equals(m2.getName())) {
			return false;
		}
		if (!m1.getGenericReturnType().getTypeName().equals(m2.getGenericReturnType().getTypeName())) {
			return false;
		}
		Type[] pt1 = m1.getGenericParameterTypes();
		Type[] pt2 = m2.getGenericParameterTypes();
		if (pt1.length != pt2.length) {
			return false;
		}
		int len = pt1.length;
		for (int i = 0; i < len; i++) {
			if (!pt1[i].getTypeName().equals(pt2[i].getTypeName())) {
				return false;
			}
		}
		return true;
	}
}
