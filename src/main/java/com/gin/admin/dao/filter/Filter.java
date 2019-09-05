package com.gin.admin.jdbc.dao.filter;

import java.io.IOException;

import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import com.gin.admin.jdbc.dao.BaseDao;

public class Filter implements TypeFilter {
	private Class<?> targetClass;

	public Filter(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	@Override
	public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
			throws IOException {
		ClassMetadata classMeta = metadataReader.getClassMetadata();
		try {
			if (classMeta.getClass().equals(BaseDao.class)) {
				return true;
			}
			return classMeta.isInterface() && classMeta.isIndependent() && targetClass
					.isAssignableFrom(Class.forName(classMeta.getClassName(), true, getClass().getClassLoader()));
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
}
