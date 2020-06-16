package com.bonc.service.data.jpa.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
//@Api(tags="测试类")
public class AppTestController {
	public static final String TEST_TEMP="Test %1$s successfully.";
	
	@Value("${spring.application.name:APP}")
	String appName;
	
	@RequestMapping(value={"","/test"}, method=RequestMethod.GET)
//	@ApiOperation(value = "app测试信息")
	public String test1() {
		return String.format(TEST_TEMP, appName);
	}

}
