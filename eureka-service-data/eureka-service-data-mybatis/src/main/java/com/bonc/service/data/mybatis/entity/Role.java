package com.bonc.service.data.mybatis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;


/**
 * 乐观锁，必须使用java.persistence.Version标记辅助字段<p>
 * @author litianlin
 * @date   2019年7月31日上午10:46:22
 * @Description TODO
 */
@Getter
@Setter
public class Role implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer rId;
	private String roleName;
	private Integer version;
	
}
