package com.bonc.service.data.mybatis.service;

import com.bonc.service.data.mybatis.entity.jpa.User;

public interface MybatisService {
	int insertUser(User user);
	int deleteUserById(Integer id);
	int updateUser(Integer id, String username);
	User findUserById(Integer id);
	User findUserByUsername(String username);
	
}
