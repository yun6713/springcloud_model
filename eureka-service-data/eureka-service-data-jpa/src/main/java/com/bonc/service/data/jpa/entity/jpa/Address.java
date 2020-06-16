package com.bonc.service.data.jpa.entity.jpa;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.hibernate.annotations.Parent;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * 复用，或构建多列id。
 * @Parent引用父级。
 * @author litianlin
 * @date   2019年7月29日下午1:22:34
 * @Description TODO
 */
@Embeddable
@Getter
@Setter
public class Address implements Serializable{
	private static final long serialVersionUID = 1L;
	//	@Id
//	@GeneratedValue(generator="uuid")
//	@GenericGenerator(name="uuid",strategy="uuid")
//	private String id;
	private String addr;
	private String zipCode;
	@Parent//引用父级
	@JsonIgnore//必须忽略，否则json序列化循环引用，报错
	private transient User user;//循环引用，增加transient修饰
}
