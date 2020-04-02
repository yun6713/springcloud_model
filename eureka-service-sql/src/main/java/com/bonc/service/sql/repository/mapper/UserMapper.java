package com.bonc.service.sql.repository.mapper;

import java.util.List;

import com.bonc.service.sql.entity.jpa.User;

public interface UserMapper{
	List<User> selectUsers();
	User insertUser(User user);
}
