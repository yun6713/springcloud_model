package com.bonc.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.lang.NonNull;

/**
 * springcloud注册中心；
 * @author litianlin
 * @date   2020年4月1日上午10:59:39
 * @Description TODO
 */

@EnableEurekaServer//eureka服务器
@SpringBootApplication
public class RegisterApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegisterApplication.class, args);
		print(null);
	}
	@NonNull
	public static String print(String str) {
		System.out.println(str);
		return null;
	}
}
