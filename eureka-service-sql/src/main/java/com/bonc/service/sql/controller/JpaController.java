package com.bonc.service.sql.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.service.sql.entity.jpa.Address;
import com.bonc.service.sql.entity.jpa.Role;
import com.bonc.service.sql.entity.jpa.User;
import com.bonc.service.sql.service.JpaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
/**
 * 用户增删改查，jpa实现。
 * 事务：声明式事务、编程式事务
 * 事务获取：EntityManager、PlatformTransactionManager
 * 查询方式：jpa、EntityManager
 * 读锁：@Lock
 * 
 * @author litianlin
 * @date   2020年4月3日上午9:33:32
 * @Description TODO
 */
@Api(tags="jpa查询。事务、多数据源、Example、EntityManager、回调、审计")
@RestController
@RequestMapping("/jpa")
public class JpaController{
	@Autowired
	JpaService jpaService;
	
	@ApiOperation("新增用户。声明式事务，模拟异常测试回滚。")
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", defaultValue="yun6713", value = "姓名", required = true, paramType = "path")})
	@RequestMapping(value="/insertUser/{name}", method=RequestMethod.GET)
	public Object insertUser(@PathVariable String name) throws Exception {
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
		addr.setUser(u);
		u.setAddr(addr);
		return jpaService.saveUser(u,true);   
	}
	  
	@ApiOperation("按id删除用户。编程式事务；txManager获取事务")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", defaultValue="1", value = "id", required = true, paramType = "path")})
	@RequestMapping(value="/deleteUser/{id}", method=RequestMethod.GET)
	public String deleteUser(@PathVariable("id") Integer id) {
		jpaService.deleteUserById(id);
		return "Success";
	}
	
	@ApiOperation("按id更新用户。")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", defaultValue="1", value = "id", required = true, paramType = "path"),
		 @ApiImplicitParam(name = "newName", defaultValue="Tian75", value = "新姓名", required = true, paramType = "body")})
	@RequestMapping(value="/updateUsername/{id}", method=RequestMethod.GET)
	public String updateUsername(@PathVariable("id") Integer id, @RequestParam("newName") String newName) throws Exception {
		User user = jpaService.findUserById(id);
		if(user != null) {
			user.setUsername(newName);
			jpaService.saveUser(user, false);
		}
		return user==null?"No such user, id="+id:"Success";
	}
	
	@ApiOperation("按id查询用户。EntityManager查询")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", defaultValue="1", value = "id", required = true, paramType = "path")})
	@RequestMapping(value="/findUser/{id}", method=RequestMethod.GET)
	public User findUser(@PathVariable("id") Integer id) {
		User u = jpaService.findUserById(id);
		return u;
	}
	
	@ApiOperation("按id查询用户。读锁，编程式事务；entityManager获取事务")
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "name", required = true, paramType = "path")})
	@RequestMapping(value="/findUserByUsername/{name}", method=RequestMethod.GET)
	public User findUserByUsername(@PathVariable("name") String name) {
		User u = jpaService.findUserByUsername(name);
		return u;
	}

	@ApiOperation("查询全部用户。jpa整合mybatis查询")
	@RequestMapping(value="/findAllUsers", method=RequestMethod.GET)
	public Object findAllUsers() {
		List<User> users=jpaService.getAllUsers();
		return users;
	} 


	@ApiOperation("新增角色。jpa保存后回调事件，审计信息。")
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", defaultValue="ROLE_admin", value = "角色名", required = true, paramType = "path")})
	@RequestMapping(value="/insertRole/{name}", method=RequestMethod.GET)
	public Object insertRole(@PathVariable String name) {
		Role role=new Role();
		role.setRoleName(name);
		return jpaService.saveRole(role);
	}

	@ApiOperation("审计信息。")
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", defaultValue="ROLE_admin", value = "角色名", required = true, paramType = "body")})
	@RequestMapping(value="/updateUser", method=RequestMethod.GET)
	public Object updateUser(@RequestBody User name) {
//		Role role=new Role();
//		role.setRoleName(name);
//		return jpaService.saveRole(role);
		return "";
	}
	
	@ApiOperation("按id删除角色。自定义注解，aop事务")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", defaultValue="1", value = "id", required = true, paramType = "path")})
	@RequestMapping(value="/deleteRole/{id}", method=RequestMethod.GET)
	public String deleteRole(@PathVariable("id") Integer id) {
		jpaService.deleteRoleById(id);
		return "Success";
	}	
	@ApiOperation("按id查询角色。乐观锁，@Query/@Modifying，@QueryHints查询配置")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", defaultValue="1", value = "id", required = true, paramType = "path")})
	@RequestMapping(value="/findRole/{id}", method=RequestMethod.GET)
	public Role findRole(@PathVariable("id") Integer id) {
		Role role = jpaService.findRoleById(id);
		return role;
	}

	@ApiOperation("查询全部角色。EntityManager命名查询nameQuery")
	@RequestMapping(value="/findAllRoles", method=RequestMethod.GET)
	public Object findAllRoles() {
		List<Role> roles=jpaService.getAllRoles();
		return roles;
	} 

	@ApiOperation("其他jpa操作。分页查询，Example查询，批量执行")
	@RequestMapping(value="/jpaOperation", method=RequestMethod.GET)
	public Object jpaOperation() {
		jpaService.findUserPage(0, 1, "username");
		jpaService.jpaOperation();
		return "";
	} 
	
}
