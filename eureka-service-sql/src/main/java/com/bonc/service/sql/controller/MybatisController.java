package com.bonc.service.sql.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.service.sql.entity.jpa.Address;
import com.bonc.service.sql.entity.jpa.Role;
import com.bonc.service.sql.entity.jpa.User;
import com.bonc.service.sql.repository.jpa.UserRepository;
import com.bonc.service.sql.repository.mapper.UserMapper;
import com.bonc.service.sql.service.JpaService;

/**
 * 用户增删改查，mybatis实现。
 * @author litianlin
 * @date   2020年4月3日上午9:33:32
 * @Description TODO
 */
@RestController
@RequestMapping("/mybatis")
public class MybatisController{
	@Autowired
	JpaService jpaService;
	@Autowired
	UserRepository ur;
	@Autowired
	UserMapper uo;
	@RequestMapping("/find/{id}")
	public String find(@PathVariable("id") Integer id) {
		User u = jpaService.findUserById(id);
		return Optional.ofNullable(u).map(User::getUsername).orElse("noSuchOne");
	}
	@RequestMapping("/findRole/{id}")
	public Role findRole(@PathVariable("id") Integer id) {
		Role r = jpaService.findRoleById(id);
		return r;
	}
	@RequestMapping("/findAllUsers")
	public Object findAllUsers() {
		List<User> mybatis=uo.selectUsers();
		return ur.selectUsers();
	}
	@RequestMapping("/insertUser/{name}")
	public Object insertUser(@PathVariable String name) {
		User u = new User();
		u.setUsername(name);
		u.setPassword("b");
		Role role = jpaService.findRoleByName("ROLE_admin");
		if(role==null) {
			role=new Role();
			role.setRoleName("ROLE_admin");
			jpaService.saveRole(role);
		}
		u.setRoles(Arrays.asList(role));
		Address addr=new Address();
		addr.setAddr("cq City");
		u.setAddr(addr);
		jpaService.saveUser(u,true);
		return u.getuId()==null?"Failed":"Success";
	}
	@RequestMapping("/insertRole/{roleName}")
	public Object insertRole(@PathVariable String roleName) {
		Role role;
		if((role=jpaService.findRoleByName(roleName))!=null) {
			return role;
		}
		role = new Role();
		role.setRoleName(roleName);
		jpaService.saveRole(role);
		return role;
	}
	@RequestMapping("/deleteUser/{id}")
	public String deleteUser(@PathVariable("id") Integer id) {
		jpaService.deleteUserById(id);
		return "Success";
	}
}
