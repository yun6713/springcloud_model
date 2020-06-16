//package com.bonc.service.mybatisPlus.mybatis;
//
//import java.util.List;
//
//import javax.sql.DataSource;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.ibatis.mapping.DatabaseIdProvider;
//import org.apache.ibatis.plugin.Interceptor;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
//import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
//import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.stereotype.Component;
//import org.springframework.util.ObjectUtils;
//import org.springframework.util.StringUtils;
//
///**
// * @author Administrator
// * @date 2020年4月28日
// * DESC: 模拟MybatisAutoConfiguration流程，简化多数据源参数传递
// * 		如不同实例需要不同配置，构建新实例。
// */
//@Component
//public class MybatisMultiDataSourceHelper {
//	private static final Logger logger = LoggerFactory.getLogger(MybatisMultiDataSourceHelper.class);
//
//	private final MybatisProperties properties;
//
//	private final Interceptor[] interceptors;
//
//	private final ResourceLoader resourceLoader;
//
//	private final DatabaseIdProvider databaseIdProvider;
//
//	private final List<ConfigurationCustomizer> configurationCustomizers;
//
//	public MybatisMultiDataSourceHelper(MybatisProperties properties, ObjectProvider<Interceptor[]> interceptorsProvider,
//			ResourceLoader resourceLoader, ObjectProvider<DatabaseIdProvider> databaseIdProvider,
//			ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
//		this.properties = properties;
//		this.interceptors = interceptorsProvider.getIfAvailable();
//		this.resourceLoader = resourceLoader;
//		this.databaseIdProvider = databaseIdProvider.getIfAvailable();
//		this.configurationCustomizers = configurationCustomizersProvider.getIfAvailable();
//	}
//
//	public MybatisProperties getProperties() {
//		return properties;
//	}
//
//	public Interceptor[] getInterceptors() {
//		return interceptors;
//	}
//
//	public ResourceLoader getResourceLoader() {
//		return resourceLoader;
//	}
//
//	public DatabaseIdProvider getDatabaseIdProvider() {
//		return databaseIdProvider;
//	}
//
//	public List<ConfigurationCustomizer> getConfigurationCustomizers() {
//		return configurationCustomizers;
//	}
//	
//	
//
//	// 复制MybatisAutoConfiguration方法，返回SqlSessionFactoryBean，便于定制
//	public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws Exception {
//		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
//		factory.setDataSource(dataSource);
//		factory.setVfs(SpringBootVFS.class);
//		if (StringUtils.hasText(this.properties.getConfigLocation())) {
//			factory.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
//		}
//		org.apache.ibatis.session.Configuration configuration = this.properties.getConfiguration();
//		if (configuration == null && !StringUtils.hasText(this.properties.getConfigLocation())) {
//			configuration = new org.apache.ibatis.session.Configuration();
//		}
//		if (configuration != null && !CollectionUtils.isEmpty(this.configurationCustomizers)) {
//			for (ConfigurationCustomizer customizer : this.configurationCustomizers) {
//				customizer.customize(configuration);
//			}
//		}
//		factory.setConfiguration(configuration);
//		if (this.properties.getConfigurationProperties() != null) {
//			factory.setConfigurationProperties(this.properties.getConfigurationProperties());
//		}
//		if (!ObjectUtils.isEmpty(this.interceptors)) {
//			factory.setPlugins(this.interceptors);
//		}
//		if (this.databaseIdProvider != null) {
//			factory.setDatabaseIdProvider(this.databaseIdProvider);
//		}
//		if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
//			factory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
//		}
//		if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
//			factory.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
//		}
//		if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
//			factory.setMapperLocations(this.properties.resolveMapperLocations());
//		}
//
//		return factory;
//	}
//	
//}
