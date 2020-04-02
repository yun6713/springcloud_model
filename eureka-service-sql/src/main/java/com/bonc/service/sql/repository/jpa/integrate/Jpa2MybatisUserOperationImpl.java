package com.bonc.service.sql.repository.jpa.integrate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bonc.service.sql.entity.jpa.User;
import com.bonc.service.sql.repository.mapper.UserMapper;
/**
 * 自定义实现类，后缀在@EnableJpaRepositories.repositoryImplementationPostfix定义<p>
 * jpa接口多继承，用于整合mybatis查询
 * 自定义接口、实现类；必须在jpa扫描路径。
 * @author litianlin
 * @date   2019年7月31日上午11:13:38
 * @Description TODO
 */
@Component
public class Jpa2MybatisUserOperationImpl implements Jpa2MybatisUserOperation{
	@Autowired
	UserMapper mapper;
	@Override
	public List<User> selectUsers() {
		return mapper.selectUsers();
	}

}
