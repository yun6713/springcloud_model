package com.bonc.service.sql.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.bonc.service.sql.entity.jpa.Role;
import com.bonc.service.sql.entity.jpa.User;
/**
 * jpa操作用户、角色接口；增删改查
 * @author litianlin
 * @date   2020年4月3日上午11:26:51
 * @Description TODO
 */
public interface JpaService {
	
	/*----------用户操作----------*/
	String saveUser(User user,boolean encryptPwd) throws Exception;

	void deleteUserById(Integer id);
	
	User findUserById(Integer id);
	User findUserByUsername(String username);
	List<User> getAllUsers();
	Page<User> findUserPage(int page, int size, String... sortFields);

	
	/*----------角色操作----------*/
	String saveRole(Role role);

	void deleteRoleById(Integer id);

	Role findRoleByName(String roleName);
	Role findRoleById(Integer id);
	List<Role> getAllRoles();

	void jpaOperation();

	
	
}
