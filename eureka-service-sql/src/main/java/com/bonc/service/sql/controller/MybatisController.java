package com.bonc.service.sql.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
import com.bonc.service.sql.service.MybatisService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 用户增删改查，mybatis实现。删改查，只返回影响条数；如需记录可重查。<p>
 * 查询：mapper、SqlSession<p>
 * 事务：编程式事务，SqlSessionFactory/SqlSession事务
 * 锁：sql中添加
 * SqlSession执行流程：Executor、StatementHandler、ParameterHandler、ResultHandler
 * mybatis拦截器：继承Interceptor，标记@Intercepts
 * 
 * 
 * @author litianlin
 * @date   2020年4月3日上午9:33:32
 * @Description TODO
 */
@Api(tags="mybatis查询。事务、拦截器、多数据源")
@RestController
@RequestMapping("/mybatis")
public class MybatisController{
	@Autowired
	MybatisService mybatisService;
	@Autowired
	JpaService jpaService;
	
	@ApiOperation("新增用户。声明式事务。")
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", defaultValue="yun6713", value = "姓名", required = true, paramType = "path")})
	@RequestMapping(value="/insertUser/{name}", method=RequestMethod.GET)
	public String insertUser(@PathVariable String name) {
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
		mybatisService.insertUser(u);
		return "Success";
	}
	  
	@ApiOperation("按id删除用户，外键删除先删外键关联。编程式事务；SqlSession提交")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", defaultValue="1", value = "id", required = true, paramType = "path")})
	@RequestMapping(value="/deleteUser/{id}", method=RequestMethod.GET)
	public String deleteUser(@PathVariable("id") Integer id) {
		mybatisService.deleteUserById(id);
		return "Success";
	}
	
	/*
	@ApiOperation("按id更新用户名。")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", defaultValue="1", value = "id", required = true, paramType = "path"),
		 @ApiImplicitParam(name = "newName", defaultValue="Tian75", value = "新姓名", required = true, paramType = "body")})
	@RequestMapping(value="/updateUsername/{id}", method=RequestMethod.GET)
	public String updateUsername(@PathVariable("id") Integer id, @RequestParam("newName") String newName) {
		int size = mybatisService.updateUser(id, newName);
		return size==0?"No such user, id="+id:"Success";
	}
	
	@ApiOperation("按id查询用户。EntityManager查询")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", defaultValue="1", value = "id", required = true, paramType = "path")})
	@RequestMapping(value="/findUser/{id}", method=RequestMethod.GET)
	public User findUser(@PathVariable("id") Integer id) {
		User u = mybatisService.findUserById(id);
		return u;
	}
	
	@ApiOperation("按id查询用户。读锁，编程式事务；entityManager获取事务")
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "name", required = true, paramType = "path")})
	@RequestMapping(value="/findUserByUsername/{name}", method=RequestMethod.GET)
	public User findUserByUsername(@PathVariable("name") String name) {
		User u = mybatisService.findUserByUsername(name);
		return u;
	}

	@ApiOperation("查询全部用户。jpa整合mybatis查询")
	@RequestMapping(value="/findAllUsers", method=RequestMethod.GET)
	public Object findAllUsers() {
		List<User> users=mybatisService.getAllUsers();
		return users;
	} 


	@ApiOperation("新增角色。jpa保存后回调事件，审计信息。")
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", defaultValue="ROLE_admin", value = "角色名", required = true, paramType = "path")})
	@RequestMapping(value="/insertRole/{name}", method=RequestMethod.GET)
	public Object insertRole(@PathVariable String name) {
		Role role=new Role();
		role.setRoleName(name);
		return mybatisService.insertRole(role);
	}

	@ApiOperation("审计信息。")
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", defaultValue="ROLE_admin", value = "角色名", required = true, paramType = "body")})
	@RequestMapping(value="/updateUser", method=RequestMethod.GET)
	public Object updateUser(@RequestBody User name) {
//		Role role=new Role();
//		role.setRoleName(name);
//		return mybatisService.saveRole(role);
		return "";
	}
	
	@ApiOperation("按id删除角色。自定义注解，aop事务")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", defaultValue="1", value = "id", required = true, paramType = "path")})
	@RequestMapping(value="/deleteRole/{id}", method=RequestMethod.GET)
	public String deleteRole(@PathVariable("id") Integer id) {
		mybatisService.deleteRoleById(id);
		return "Success";
	}	
	@ApiOperation("按id查询角色。乐观锁，@Query/@Modifying，@QueryHints查询配置")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", defaultValue="1", value = "id", required = true, paramType = "path")})
	@RequestMapping(value="/findRole/{id}", method=RequestMethod.GET)
	public Role findRole(@PathVariable("id") Integer id) {
		Role role = mybatisService.findRoleById(id);
		return role;
	}

	@ApiOperation("查询全部角色。EntityManager命名查询nameQuery")
	@RequestMapping(value="/findAllRoles", method=RequestMethod.GET)
	public Object findAllRoles() {
		List<Role> roles=mybatisService.getAllRoles();
		return roles;
	}

	@ApiOperation("其他jpa操作。分页查询，Example查询，批量执行")
	@RequestMapping(value="/jpaOperation", method=RequestMethod.GET)
	public Object jpaOperation() {
		mybatisService.findUserPage(0, 1, "username");
		mybatisService.jpaOperation();
		return "";
	}  */
}
