package com.bonc.consumer.feign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * springcloud消费者，feign；
 * @author litianlin
 * @date   2020年4月1日上午10:59:39
 * @Description TODO
 */

@EnableEurekaClient//eureka客户端
@SpringBootApplication
public class ConsumerFeignApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerFeignApplication.class, args);
	}

}
