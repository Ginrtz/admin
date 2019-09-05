package com.gin.admin.jdbc.dao.factory;

import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import com.gin.admin.jdbc.dao.filter.Filter;

public class Scanner extends ClassPathBeanDefinitionScanner {
	public Scanner(BeanDefinitionRegistry registry, Class<?> clazz) {
		super(registry, false);
		addIncludeFilter(new Filter(clazz));
	}

	@Override
	protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
		Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
		GenericBeanDefinition definition;
		for (BeanDefinitionHolder holder : beanDefinitions) {
			definition = (GenericBeanDefinition) holder.getBeanDefinition();
			definition.getPropertyValues().add("proxy", getRegistry().getBeanDefinition("daoProxy"));
			definition.getPropertyValues().add("objectType", definition.getBeanClassName());
			definition.setBeanClass(DaoFactory.class);
		}
		return beanDefinitions;
	}

	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
	}
}
