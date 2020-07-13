package com.bonc.service.data.mybatis.service.impl;

import java.util.Objects;
import java.util.Optional;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import com.bonc.facade.dto.data.UserDTO;
import com.bonc.facade.vo.data.UserVO;
import com.bonc.service.data.mybatis.entity.User;
import com.bonc.service.data.mybatis.repository.mapper.UserMapper;
import com.bonc.service.data.mybatis.service.MybatisService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Administrator
 * @date 2020年4月26日
 * DESC: 
 */
@Service
@Slf4j
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
	public int insertUser(UserDTO user) {
		User u = new User();
		BeanUtils.copyProperties(user, u);
		return userMapper.insertUser(u);		
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
			num = mapper.deleteUserById(id);
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

	@Override
	public int updateUser(Integer id, String username) {
		int num = userMapper.updateUser(username, id);
		return num;
	}

	@Override
	public UserVO findUserById(Integer id) {
		User u = userMapper.findUserById(id);
		return user2VO(u, UserVO.class);
	}

	@Override
	public UserVO findUserByUsername(String username) {
		User u = userMapper.findUserByUsername(username);
		
		return user2UserVO(u);
	}
	
	private UserVO user2UserVO(User u){
		return Optional.ofNullable(u)
				.map(item->{
					UserVO obj = new UserVO();
					BeanUtils.copyProperties(item, obj);
					return obj;
				}).orElse(null);
	}
	
	/**
	 * 用户user转vo
	 * @param u
	 * @param targetClazz
	 * @return
	 */
	public static <T> T user2VO(User u, Class<T> targetClazz){
		Objects.requireNonNull(targetClazz, "targetClazz can't be null.");
		return Optional.ofNullable(u)
				.map(item->{
					T t = null;
					try {
						t = targetClazz.newInstance();
						BeanUtils.copyProperties(item, t);
					} catch (Exception e) {
						log.error("MybatisServiceImpl.user2VO error, u:{}, targetClazz:{}, e:{}", u, targetClazz.getCanonicalName(), e);;
					}
					return t;
				}).orElse(null);
	}
}
