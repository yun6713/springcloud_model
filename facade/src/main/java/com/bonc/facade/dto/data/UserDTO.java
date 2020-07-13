package com.bonc.facade.dto.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UserDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer uId;
	@ApiModelProperty(value="姓名",example="ltl")
	private String username;
	private String password;
	private Date createTime;
	private Date updateTime;
	private String test;
	private Collection<RoleDTO> roles=new ArrayList<>();
	
	
}
