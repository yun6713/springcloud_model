package com.bonc.consumer.feign.service.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bonc.facade.constant.ServiceDataMybatis;
import com.bonc.facade.vo.data.UserVO;

@FeignClient(value=ServiceDataMybatis.APP_NAME, path="mybatis")
public interface MybatisUserService {
	
	@RequestMapping(value="/insertUser/{name}", method=RequestMethod.GET)
	String insertUser(String name);
	
	@RequestMapping(value="/deleteUser/{id}", method=RequestMethod.GET)
	public String deleteUser(@PathVariable("id") Integer id);
	
	@RequestMapping(value="/updateUsername/{id}", method=RequestMethod.GET)
	public String updateUsername(@PathVariable("id") Integer id, @RequestParam("newName") String newName);
	
	@RequestMapping(value="/findUser/{id}", method=RequestMethod.GET)
	public UserVO findUser(@PathVariable("id") Integer id);
	
	@RequestMapping(value="/findUserByUsername/{name}", method=RequestMethod.GET)
	public UserVO findUserByUsername(@PathVariable("name") String name);
		
	
}
