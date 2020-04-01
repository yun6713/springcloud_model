package com.bonc.consumer.ribbon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * springcloud消费者，ribbon；
 * @author litianlin
 * @date   2020年4月1日上午10:59:39
 * @Description TODO
 */

@EnableEurekaClient//eureka客户端
@SpringBootApplication
public class ConsumerRibbonApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerRibbonApplication.class, args);
	}

}
