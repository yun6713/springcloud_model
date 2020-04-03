package com.bonc.service.sql.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.bonc.service.sql.aop.ProgramTransaction;
import com.bonc.service.sql.entity.jpa.Role;
import com.bonc.service.sql.entity.jpa.User;
import com.bonc.service.sql.repository.jpa.RoleRepository;
import com.bonc.service.sql.repository.jpa.UserRepository;
import com.bonc.service.sql.service.JpaService;
@Service
public class JpaServiceImpl implements JpaService{
	private static final Logger LOG = LoggerFactory.getLogger(JpaServiceImpl.class);
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	@PersistenceContext//em线程不安全；标记em，注入代理类。
	EntityManager entityManager;
	/*
	 * spring事务管理器，
	 * 也可用 TransactionTemplate，其继承DefaultTransactionDefinition
	 */
	@Autowired
	@Qualifier("jpaTransactionManager")
	PlatformTransactionManager txManager;
	@Autowired
	PasswordEncoder pe;

	@Override
	@NonNull//参数、返回值不可为null
	@Transactional(transactionManager="jpaTransactionManager")
	public String saveUser(User user,boolean encryptPwd) {
		//新增
		if(user.getuId()==null) {
			//重名
			if(userRepo.findByUsername(user.getUsername())!=null) {
				return "重名";
			}
			//加密密码
			if(encryptPwd) {
				user.setPassword(pe.encode(user.getPassword()));
			}
			userRepo.saveAndFlush(user);
			return "Save success";
		}else {
			//更新
			userRepo.save(user);
			return "Update success";
		}
	}

	@Override
	public void deleteUserById(Integer id) {
		executeInTxManager(txManager, null, ()->userRepo.deleteById(id));	
	}
	
	@FunctionalInterface
	public static interface SqlHandler {
		void handle();
	}
	public static TransactionStatus executeInTxManager(PlatformTransactionManager txManager, TransactionDefinition txDef, SqlHandler sh) {
		if(txDef == null) {
			txDef = new DefaultTransactionDefinition();
		}
		TransactionStatus txStatus = null;
		try {
			txStatus = txManager.getTransaction(txDef);
			sh.handle();
			txManager.commit(txStatus);
		} catch (Exception e) {
			LOG.error("deleteUserById error: {}", e);
			if(txStatus != null) {
				txManager.rollback(txStatus);
			}
		}
		return txStatus;
	}
	@Override
	public User findUserById(Integer id) {
		User u=entityManager.find(User.class, id);
		
		return u;
	}
	/**
	 * repo加乐观锁，编程式事务；
	 */
	@Override
	public User findUserByUsername(String username) {
		EntityTransaction tansaction=entityManager.getTransaction();
		try {
			tansaction.begin();
			User u=userRepo.findByUsername(username);
			tansaction.commit();
			return u;
		} catch (Exception e) {
			tansaction.rollback();
			LOG.error("findUserById error: {}", e);
		}
		return null;
	}
	
	//整合mybatis查询
	@Override
	public List<User> getAllUsers() {
		return userRepo.selectUsers();
	}
	
	@Override
	public String saveRole(Role role) {
		//新增
		if(role.getrId()==null) {
			//重名
			if(roleRepo.findByRoleName(role.getRoleName())!=null) {
				return "重名";
			}
			roleRepo.saveAndFlush(role);
			return "Save role success";
		}else {
			//更新
			roleRepo.save(role);
			return "Update role success";
		}
	}
	@Transactional(transactionManager="jpaTransactionManager")
	@Override
	public Role findRoleById(Integer id) {
		return roleRepo.findByRId(id).orElse(null);
	}
	@Override
 	@ProgramTransaction(txManagerName="jpaTransactionManager")
	public void deleteRoleById(Integer id) {
		roleRepo.deleteById(id);
	}
	@Override
	@Cacheable()
	public Role findRoleByName(String roleName) {
		return roleRepo.findByRoleName(roleName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getAllRoles() {
		//entityManager query查询；可包装sql
		return entityManager.createNamedQuery("Role.getAllRoles")
				.getResultList();
	}
	
}
