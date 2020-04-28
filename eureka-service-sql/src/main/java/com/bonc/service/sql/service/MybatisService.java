package com.bonc.service.sql.service;

import com.bonc.service.sql.entity.jpa.Role;
import com.bonc.service.sql.entity.jpa.User;

public interface MybatisService {
	int insertUser(User user);
	int deleteUserById(Integer id);
	/*int updateUser(Integer id, String username);
	User findUserById(Integer id);
	User findUserByUsername(String username);

	int insertRole(Role role);
	int deleteRoleById(Integer id);
	int updateRole(Role role);
	Role findRoleById(Integer id);
	Role findRoleByName(String roleName);*/
	
}
