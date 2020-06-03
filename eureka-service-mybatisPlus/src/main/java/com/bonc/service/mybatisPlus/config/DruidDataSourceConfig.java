package com.bonc.service.mybatisPlus.config;

import java.util.List;
import java.util.Objects;
import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;

import cn.hutool.core.map.MapUtil;

/**
 * 配置druid、监控界面
 * @author litianlin
 * @date   2019年7月31日下午1:17:45
 * @Description TODO
 */
@Configuration
public class DruidDataSourceConfig {
	@Bean("firstDataSource")
	@ConfigurationProperties(prefix="spring.datasource.first")
	public Properties firstDataSource() {
		return new Properties();
	}

	@Bean("druidConfig")
	@ConfigurationProperties(prefix="spring.datasource.druid")
	public Properties druidConfig() {
		return new Properties();
	}
	
//	使用@ConfigurationProperties注入属性时，部分属性名称无法匹配，从而未注入；手动配置
	@Bean("h2DataSource")
	@Primary
	public DruidDataSource h2DataSource() {
		DruidDataSource dds = configDruidDataSource(firstDataSource(),druidConfig());
		List<Filter> filters=dds.getProxyFilters();
		filters.add(wallFilter());
		return dds;
	}
	/**
	 * 手动配置防火墙，防止不必要报错
	 * @return
	 */
	@Bean
	public WallFilter wallFilter() {
		WallConfig config=new WallConfig();
		config.setNoneBaseStatementAllow(true);//执行DDL		
		WallFilter wall=new WallFilter();	
		wall.setConfig(config);
		//sql解析错误时处理策略，log.err输出，不抛异常
		wall.setLogViolation(true);
		wall.setThrowException(false);
		return wall;
	}
	private DruidDataSource configDruidDataSource(Properties dbProps,Properties dsProps){
		Properties p = new Properties();
		if(dbProps!=null)
			dbProps.forEach((k,v)->{p.setProperty("druid."+k, Objects.toString(v));});
		if(dsProps!=null)
			dsProps.forEach((k,v)->{p.setProperty("druid."+k, Objects.toString(v));});
		DruidDataSource dds = DruidDataSourceBuilder.create().build();
		dds.configFromPropety(p);
		return dds;
	}
	
//druid浏览器访问
	@Bean
	public FilterRegistrationBean<WebStatFilter> druidFilter(){
		FilterRegistrationBean<WebStatFilter> frb=new FilterRegistrationBean<>();
		frb.setFilter(new WebStatFilter());
		frb.setInitParameters(MapUtil.builder("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*").build());
		frb.addUrlPatterns("/*");
		frb.setName("druidWebStatFilter");
		return frb;
	}
	//可从外部读入配置
	@Bean
	public ServletRegistrationBean<StatViewServlet> druidServlet(){
		ServletRegistrationBean<StatViewServlet> srb=new ServletRegistrationBean<>();
		srb.setServlet(new StatViewServlet());
		srb.setInitParameters(MapUtil.builder("loginUsername","admin")
				.put("loginPassword", "admin")
				.put("resetEnable", "false")
				.put("allow", "192.168.16.117,127.0.0.1")
				.put("deny", "192.168.16.118")
				.build());
		srb.addUrlMappings("/druid/*");
		return srb;
	}
}
