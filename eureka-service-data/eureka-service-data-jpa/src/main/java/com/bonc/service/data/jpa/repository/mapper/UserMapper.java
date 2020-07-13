package com.bonc.service.data.jpa.repository.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bonc.service.data.jpa.entity.User;
@Mapper
public interface UserMapper{
	int insertUser(User user);
	int deleteUserRole(@Param("id") Integer id);
	int deleteUser(@Param("id") Integer id);
	/*int updateUser(String username, Integer id);
	User findUserById(Integer id);
	User findUserByUsername(String username);*/
	List<User> selectUsers();
	
	
}
