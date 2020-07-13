package com.bonc.service.data.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

//@Entity
//@Table(name="user_role")
@Getter
@Setter
public class UserRole implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="ur_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String urId;
	@Column(name="u_id")
	private String uId;
	@Column(name="r_id")
	private String rId;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((urId == null) ? 0 : urId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserRole other = (UserRole) obj;
		if (urId == null) {
			if (other.urId != null)
				return false;
		} else if (!urId.equals(other.urId))
			return false;
		return true;
	}
	
	
}
