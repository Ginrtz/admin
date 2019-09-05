package com.gin.admin.jdbc.dao.factory;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;

public class DaoFactory<T> implements FactoryBean<T> {
	private Class<?> objectType;
	private DaoProxy proxy;

	public DaoProxy getProxy() {
		return proxy;
	}

	public void setProxy(DaoProxy proxy) {
		this.proxy = proxy;
	}

	public void setObjectType(Class<?> objectType) {
		this.objectType = objectType;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getObject() throws Exception {
		return (T) Proxy.newProxyInstance(objectType.getClassLoader(), new Class[] { objectType }, proxy);
	}

	@Override
	public Class<?> getObjectType() {
		return objectType;
	}
}
