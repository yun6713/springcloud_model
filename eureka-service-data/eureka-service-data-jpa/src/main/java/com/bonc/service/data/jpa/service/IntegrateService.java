package com.bonc.service.data.jpa.service;

import com.bonc.service.data.jpa.entity.Role;
import com.bonc.service.data.jpa.entity.User;

public interface IntegrateService {
	User saveUser(User user,boolean encrypt);
	User findUserById(Integer id);
	User findUserByUsername(String username);
	void deleteUserById(Integer id);
	
	Role saveRole(Role role);
	Role findRoleById(Integer id);
	void deleteRoleById(Integer id);
	Role findRoleByName(String roleName);
	
}
