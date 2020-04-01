package com.bonc.consumer.feign.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AppTestController {
	public static final String TEST_TEMP="Test %1$s successfully.";
	
	@Value("${spring.application.name:APP}")
	String appName;
	
	@RequestMapping({"","/test"})
	@ResponseBody
	public String test1() {
		return String.format(TEST_TEMP, appName);
	}
	
	@RequestMapping("/testBaidu")
	public String test2() {
		return "forward:http://www.baidu.com";
	}

}
