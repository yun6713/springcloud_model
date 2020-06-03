package com.bonc.service.sql.service.impl;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.service.sql.entity.jpa.User;
import com.bonc.service.sql.repository.mapper.UserMapper;
import com.bonc.service.sql.service.MybatisService;

/**
 * @author Administrator
 * @date 2020年4月26日
 * DESC: 
 */
@Service
public class MybatisServiceImpl implements MybatisService {
	private static final Logger LOG = LoggerFactory.getLogger(MybatisServiceImpl.class);
	@Autowired
	private UserMapper userMapper;
	@Autowired
	SqlSessionFactory ssf;
	@Autowired
	@Qualifier("mybatisTransactionManager")
	PlatformTransactionManager txManager;
	
	@Override
	@Transactional("mybatisTransactionManager")
	public int insertUser(User user) {
		return userMapper.insertUser(user);		
	}

	@Override
	public int deleteUserById(Integer id) {
		int num = 0;
		/*
		 * 默认开启事务，不自动提交。sqlSession必须关闭。
		 * 先删外键关联表
		 */
		//null为默认DefaultTransactionDefinition
		TransactionStatus txStatus = txManager.getTransaction(null);
		try(SqlSession sqlSession = ssf.openSession()){
			txStatus.createSavepoint();
			UserMapper mapper = sqlSession.getMapper(UserMapper.class);
			mapper.deleteUserRole(id);
			num = mapper.deleteUser(id);
			if(id == 1){
				throw new RuntimeException("Test mybatis transaction");
			}
			txManager.commit(txStatus);
		}catch (Exception e) {
			if(LOG.isDebugEnabled()) {
				LOG.error("error msg stacktrace:{}",e);
			}else {
				LOG.error("error msg:{}",e.getMessage());
			}
			//未提交，可回滚。提交时异常，不可回滚。
			if(txStatus.isCompleted()) {
				txManager.rollback(txStatus);
			}
		}
		return num;
	}
/*
	@Override
	public int updateUser(Integer id, String username) {
		int num = userMapper.updateUser(username, id);
		return num;
	}

	@Override
	public User findUserById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User findUserByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insertRole(Role role) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteRoleById(Integer id) {
		return 0;
	}

	@Override
	public int updateRole(Role role) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Role findRoleById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Role findRoleByName(String roleName) {
		// TODO Auto-generated method stub
		return null;
	}
*/
	
}
