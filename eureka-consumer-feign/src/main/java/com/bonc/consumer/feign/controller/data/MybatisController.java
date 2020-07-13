package com.bonc.consumer.feign.controller.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.consumer.feign.service.data.MybatisUserService;

@RestController("mybatis")
public class MybatisController {
	@Autowired
	private MybatisUserService mybatisUserService;

	
}
