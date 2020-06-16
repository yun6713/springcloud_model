package com.bonc.service.data.jpa.repository.jpa;
import java.util.Optional;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.bonc.service.data.jpa.entity.jpa.Role;

public interface RoleRepository extends JpaRepository<Role,Integer> {

	Role findByRoleName(String roleName);
	@Lock(LockModeType.READ)//乐观锁，必须包含在事务中，实体类必须有java.persistence.Version标记的辅助字段
	//设置锁超时时间
	@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
	//nativeQuery是否直接数据库查询，变量格式--?1、:pName；Sort、Pageable参数置于形参列表最后，不需引用。
	@Query(name="findByRId", nativeQuery=true, value="select r from Role role where role.id=:rId")
	//DML语句标记修改
	//@Modifying
	Optional<Role> findByRId(Integer rId);
}
