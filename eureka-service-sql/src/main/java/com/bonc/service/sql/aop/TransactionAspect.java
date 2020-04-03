package com.bonc.service.sql.aop;

import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Aspect
@Component
@EnableAspectJAutoProxy
public class TransactionAspect {
	private static final Logger LOG = LoggerFactory.getLogger(TransactionAspect.class);
	@Autowired
	Map<String,PlatformTransactionManager> txManagerMap;
	
	@Pointcut(value="@annotation(pt)", argNames="pt")
	public void transaction(ProgramTransaction pt) {};
	//事务执行，打印方法信息
	@Around(value="transaction(pt)", argNames="pt")
	public Object surround(ProceedingJoinPoint pjp, ProgramTransaction pt) {
		String info = pjp.toLongString();
		LOG.info("[START] transaction execute: {}", info);
		DefaultTransactionDefinition txDef = new DefaultTransactionDefinition();
		PlatformTransactionManager txManager = txManagerMap.get(pt.txManagerName());
		TransactionStatus txStatus = null;
		Object result = null;
		try {
			txStatus = txManager.getTransaction(txDef);
			result = pjp.proceed();
			txManager.commit(txStatus);
			LOG.info("[SUCCESS] transaction execute: {}", info);
			return result;
		} catch (Throwable t) {
			LOG.error("[ERROR] transaction execute:{}; Error: {}", t);
			if(txStatus != null) {
				txManager.rollback(txStatus);
			}
		}
		return result;
	}
}
