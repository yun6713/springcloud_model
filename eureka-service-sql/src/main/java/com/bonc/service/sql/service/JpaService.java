package com.bonc.service.sql.service;

import com.bonc.service.sql.entity.jpa.Role;
import com.bonc.service.sql.entity.jpa.User;

public interface JpaService {
	User saveUser(User user,boolean encrypt);
	User findUserById(Integer id);
	User findUserByUsername(String username);
	void deleteUserById(Integer id);
	
	Role saveRole(Role role);
	Role findRoleById(Integer id);
	void deleteRoleById(Integer id);
	Role findRoleByName(String roleName);
	
}
