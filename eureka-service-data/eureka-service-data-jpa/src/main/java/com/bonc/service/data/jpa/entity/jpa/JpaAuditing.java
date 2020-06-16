package com.bonc.service.data.jpa.entity.jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

/**
 * jpa审计
 * @CreatedDate、@LastModifiedDate；时间
 * @CreatedBy、@LastModifiedBy；用户，需实现AuditorAware以提供用户信息
 * @author Administrator
 *
 */
@MappedSuperclass//审计信息基类，不对应表。
@EntityListeners(value = { AuditingEntityListener.class })//审计信息回调类
@Getter
@Setter
public class JpaAuditing implements Serializable{
	private static final long serialVersionUID = 1L;
	@CreatedBy
	protected String creater;
	@CreatedDate
	protected Date createDate;
	@LastModifiedBy
	protected String updater;
	@LastModifiedDate
	protected Date updateDate;
}
