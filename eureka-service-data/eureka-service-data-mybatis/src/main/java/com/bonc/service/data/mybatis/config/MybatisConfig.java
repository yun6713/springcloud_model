package com.bonc.service.data.mybatis.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;

import com.bonc.service.data.mybatis.mybatis.MybatisMultiDataSourceHelper;
import com.bonc.service.data.mybatis.repository.mapper.MybatisMarker;

import cn.hutool.core.map.MapUtil;
/**
 * 配置mybatis SQLSessionFactoryBean（dataSource\transactionManager\mapperLocation）<p>
 * mybatis跨库：配置DatabaseIdProvider，sql标签的databaseId属性，优先使用databaseId匹配的标签；<p>
 * 设置类型别名包，SqlSessionFactoryBean#setTypeAliasesPackage；别名默认类名首字母小写，@Alias指定。<p>
 * 日志扫描顺序：slf4j、jcl、log4j2、log4j、jul；<p>
 * springboot可通过mybatis.configuration.*进行完全控制<p>
 * 
 * @author litianlin
 * @date   2019年7月5日下午12:57:54
 * @Description TODO
 */
@Configuration
@MapperScan(basePackageClasses= {MybatisMarker.class}
	,sqlSessionFactoryRef="firstMybatis"
)
public class MybatisConfig {
	@Autowired
	DataSource dataSource;
	
	/*
	 * 通过springboot方式，直接配置多数据源
	 * 不可通过MybatisAutoConfiguration配置，循环报错。
	 * @Bean方法中，避免调用同返回实例且为默认自动配置的@Bean方法，否则循环报错。
	 */
	@Bean("firstMybatis")
	public SqlSessionFactoryBean first(MybatisMultiDataSourceHelper config) throws Exception {
		//事务由spring管理，无需事务配置.
		return config.sqlSessionFactory(dataSource);	
	}
	@Bean
	public ConfigurationCustomizer customizer() {		
		return config -> {
//			config.addInterceptor(null);
			System.out.println("[ConfigurationCustomizer]");
		};
	}

	
	/*
	 * 配置跨库支持，databaseName--databaseId关联
	 * 关联信息可从文件读取。
	 */
	@Bean
	public DatabaseIdProvider databaseIdProvider(){
		DatabaseIdProvider databaseIdProvider=new VendorDatabaseIdProvider();
		Properties p=new Properties();
		p.putAll(MapUtil.builder("SQL Server", "sqlserver")
				.put("MySQL", "mysql")
				.put("Oracle", "oracle")
				.put("DB2", "db2")
				.put("H2", "h2").build());
		databaseIdProvider.setProperties(p);
		return databaseIdProvider;
	}
	// 事务配置，用于@Transactional(spring)，绑定编程式事务。
	@Bean(name = "mybatisTransactionManager")
	public PlatformTransactionManager mybatisTransactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}
	/**
	 * 加密策略，托管给spring，便于保存用户时加密密码
	 * encode()，加密密码
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder(){
		//不加密，已过时
//		return NoOpPasswordEncoder.getInstance();
		return new BCryptPasswordEncoder();
	}
}
