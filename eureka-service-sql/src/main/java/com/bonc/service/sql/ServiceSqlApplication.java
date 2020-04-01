package com.bonc.service.sql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * springcloud服务；h2、druid、mybatis、hibernate
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
