package com.bonc.service.sql.service.impl;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.service.sql.entity.jpa.Role;
import com.bonc.service.sql.entity.jpa.User;
import com.bonc.service.sql.repository.jpa.RoleRepository;
import com.bonc.service.sql.repository.jpa.UserRepository;
import com.bonc.service.sql.repository.mapper.UserMapper;
import com.bonc.service.sql.service.JpaService;
@Service
public class JpaServiceImpl implements JpaService{
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	UserMapper userOp;
	@Autowired
	EntityManager entityManager;
	Cache redisCache;
	@Autowired
	PasswordEncoder pe;

	@Override
	public User saveUser(User user,boolean encrypt) {
		if(user.getuId()==null&&userRepo.findByUsername(user.getUsername())!=null)
			return user;
		//加密密码
		if(encrypt) {
			user.setPassword(pe.encode(user.getPassword()));
		}
		return userRepo.save(user);
	}
	@Transactional(transactionManager="primaryTransactionManager")
	@Override
	public User findUserById(Integer id) {
//		List list = entityManager.createNativeQuery("select * from user").getResultList();
//		List<User> users = uo.selectUsers();
//		users = ur.selectUsers();
		User u=userRepo.findByUId(id);
		return u;
	}
	@Override
	@Cacheable(cacheManager="cacheManager",cacheNames="data")
	public User findUserByUsername(String username) {		
		User user;
		if(redisCache.get(username)!=null) {
			user= (User) redisCache.get(username).get();
		}else {
			user=userRepo.findByUsername(username);
			redisCache.putIfAbsent(username, user);
		}
		return userRepo.findByUsername(username);
	}
	@Override
	public void deleteUserById(Integer id) {
		userRepo.deleteById(id);
	}
	@Override
	public Role saveRole(Role role) {
		return roleRepo.save(role);
	}
	@Transactional(transactionManager="primaryTransactionManager")
	@Override
	public Role findRoleById(Integer id) {
		return roleRepo.findByRId(id).orElse(null);
	}
	@Override
	public void deleteRoleById(Integer id) {
		roleRepo.deleteById(id);
	}
	@Override
	@Cacheable()
	public Role findRoleByName(String roleName) {
		return roleRepo.findByRoleName(roleName);
	}
	
}
