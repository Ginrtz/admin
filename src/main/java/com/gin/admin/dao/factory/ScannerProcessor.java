package com.gin.admin.jdbc.dao.factory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.gin.admin.jdbc.dao.IDAO;

@Configuration
public class ScannerConfig implements BeanDefinitionRegistryPostProcessor {
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		GenericBeanDefinition proxyDefinition = new GenericBeanDefinition();
		proxyDefinition.setBeanClass(DaoProxy.class);
		registry.registerBeanDefinition("daoProxy", proxyDefinition);
		Scanner scanner = new Scanner(registry, IDAO.class);
		scanner.scan(StringUtils.tokenizeToStringArray("com.gin.admin.jdbc.dao",
				ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
	}
}
