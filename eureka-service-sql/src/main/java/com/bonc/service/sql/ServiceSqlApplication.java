package com.bonc.service.sql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * springcloud服务；h2、druid、mybatis、hibernate<p>
 * 
 * 1，初始化数据。sql脚本。<p>
 * 2，druid、swagger配置<p>
 * 3，mybatis查询<p>
 * 4，jpa查询<p>
 * 5，jpa、mybatis组合<p>
 * 6，querydsl；<p>
 * @author litianlin
 * @date   2020年4月1日上午10:59:39
 * @Description TODO
 */

@EnableEurekaClient//eureka客户端
@SpringBootApplication
public class ServiceSqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceSqlApplication.class, args);
		
	}

}
