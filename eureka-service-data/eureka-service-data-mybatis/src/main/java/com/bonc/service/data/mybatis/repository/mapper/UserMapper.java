package com.bonc.service.data.mybatis.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bonc.service.data.mybatis.entity.jpa.User;
@Mapper
public interface UserMapper{
	int insertUser(User user);
	int deleteUserById(@Param("id") Integer id);
	User findUserById(Integer id);
	User findUserByUsername(String username);
	int updateUser(@Param("username") String username, @Param("id") Integer id);
	
	
	
}
