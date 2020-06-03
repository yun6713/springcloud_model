package com.bonc.service.sql.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
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
	@Transactional(transactionManager="jpaTransactionManager"
			/*
			 * 事务传播属性，Propagation枚举类。REQUIRED必须；REQUIRED_NEW必须新增，挂起当前事务
			 * MANDATORY必须已有事务；SUPPORTS可在事务中执行；NOT_SUPPORTED不支持事务，如有则挂起事务执行；NEVER不允许事务，有事务抛异常
			 * NEST嵌套事务，提交/回滚时不影响外层事务，外层事务回滚时回滚嵌套事务。
			 */
			, propagation=Propagation.REQUIRED
			/*
			 * 事务隔离级别，Isolation枚举类。DEFAULT默认；READ_UNCOMMITTED未提交读；READ_COMMITTED提交读
			 * REPEATABLE_READ可重复读；SERIALIZABLE可串行化
			 */
			, isolation=Isolation.DEFAULT
			//不回滚的异常。rollbackFor回滚的异常。
			, noRollbackFor=RuntimeException.class
			//只读
			, readOnly=false
			//超时
			, timeout=30)
	public String saveUser(User user,boolean encryptPwd) throws Exception {
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
			user = userRepo.saveAndFlush(user);
			//用户名结尾为t，测试不回滚；为t1，测试回滚
			if(user.getUsername().endsWith("t")){
				throw new RuntimeException("Jpa test no rollback");
			}else if(user.getUsername().endsWith("t1")) {
				throw new Exception("Jpa test rollback");
			}
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
	
	//jpa分页，page从0开始，
	@Override
	public Page<User> findUserPage(int page, int size, String... sortFields) {
		Sort sort = Sort.by(sortFields);
		Pageable pageInfo = PageRequest.of(0, 1, sort);
		return userRepo.findAll(pageInfo);
	}
	
	@Override
	@ProgramTransaction(txManagerName="jpaTransactionManager")
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
 	public void deleteRoleById(Integer id) {
		EntityTransaction tansaction=entityManager.getTransaction();
		try {
			tansaction.begin();
			roleRepo.deleteById(id);
			if(id>0) {
				throw new RuntimeException("EntityManager transaction test");
			}
			tansaction.commit();
		} catch (Exception e) {
			tansaction.rollback();
			LOG.error("findUserById error: {}", e);
		}
	}
	@Override
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
	
	@Override
	public void jpaOperation() {
		/*Example查询，无法比较时间，早于晚于等。
		 * Example/ExampleMathcher；创建匹配
		 * GenericPropertyMatcher/GenericPropertyMatchers；匹配器
		 * 
		 * */
		ExampleMatcher matcher = ExampleMatcher.matchingAny()
				.withMatcher("username", ExampleMatcher.GenericPropertyMatchers.exact())
				.withMatcher("createTime", t -> t.caseSensitive().contains())
				.withIgnoreNullValues()//忽略null
				.withStringMatcher(StringMatcher.EXACT)//String默认匹配相等
				.withIgnorePaths("u_id");//忽略属性
		User user = new User();
		user.setUsername("ltl");
		Example<User> example = Example.of(user, matcher);
		userRepo.count(example);		
		userRepo.findOne(example);//只返回一个，或null
		
		/*
		 * 批量执行
		 */
		userRepo.deleteInBatch(new ArrayList<User>());
		
		
		
	}
	
}
