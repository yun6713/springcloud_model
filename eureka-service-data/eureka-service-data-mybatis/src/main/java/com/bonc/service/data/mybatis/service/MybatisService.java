package com.bonc.service.data.mybatis.service;

import com.bonc.facade.dto.data.UserDTO;
import com.bonc.facade.vo.data.UserVO;

public interface MybatisService {
	int insertUser(UserDTO user);
	int deleteUserById(Integer id);
	int updateUser(Integer id, String username);
	UserVO findUserById(Integer id);
	UserVO findUserByUsername(String username);
	
}
