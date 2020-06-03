package com.bonc.service.sql.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bonc.service.sql.controller.AppTestController;

import io.swagger.annotations.Api;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * 通过@ApiModel、@ApiModelProperty；设置返回model
 * 通过@ApiImplicitParam；设置查询参数
 * 
 * @author Administrator
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Value("${spring.application.name}")
	String appName;
	//配置swagger选择器、api信息
	@Bean
	public Docket api() {
		
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
	            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))//筛选handler
	            .paths(PathSelectors.any())//筛选path
	            .build()
	            .apiInfo(apiInfo());
	}
	@Bean
	public Docket api2() {
		
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("ltl")//区分多个Docket
				.select()
	            .apis(RequestHandlerSelectors.basePackage(AppTestController.class.getPackage().getName()))//筛选handler
	            .paths(PathSelectors.any())//筛选path
	            .build()
	            .apiInfo(apiInfo());
	}
	//api信息；也可通过ApiInfoBuilder构建。
	private ApiInfo apiInfo() {
		return new ApiInfo(
	            "springcloud_model项目集成 Swagger 实例文档",
	            appName,
	            "API V1.0",
	            "Terms of service",
	            new Contact("ltl", "http://springfox.github.io/springfox/docs/current/", "litianlin@bonc.com.cn"),
                "Apache", 
                "http://www.apache.org/", 
                Collections.emptyList());
	}
}
