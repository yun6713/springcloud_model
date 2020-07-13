package com.bonc.service.data.jpa.repository.jpa.integrate;

import java.util.List;

import com.bonc.service.data.jpa.entity.User;
//@NoRepositoryBean//不暴露为bean,有自定义实现类时不可标记
public interface Jpa2MybatisUserOperation {
	List<User> selectUsers();
}
