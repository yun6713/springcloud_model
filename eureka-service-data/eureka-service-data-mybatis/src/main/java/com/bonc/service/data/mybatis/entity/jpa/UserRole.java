package com.bonc.service.data.mybatis.entity.jpa;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRole implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String urId;
	private String uId;
	private String rId;	
	
}
