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
	public Object surround(ProceedingJoinPoint pjp, ProgramTransaction pt) throws Throwable {
		String info = pjp.toLongString();
		//pjp.getArgs()无参时返回空数组
		Object[] args = null;
		//调试时输出参数信息
		if(LOG.isDebugEnabled()) {
			args = pjp.getArgs();
			LOG.info("[START transaction execute]: {}, args:{}", info, args);
		}		
		String txName = pt.txManagerName();
		PlatformTransactionManager txManager = txManagerMap.get(txName);
		//无对应事务管理器，抛异常
		if(txManager==null) {
			LOG.error("No txManager with name={}", txName);
			throw new Exception("No txManager with name=txName");
		}
		DefaultTransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = null;
		Object result = null;
		try {
			txStatus = txManager.getTransaction(txDef);
			result = pjp.proceed();
			txManager.commit(txStatus);
			LOG.info("[SUCCESS transaction execute]: {}", info);
			return result;
		} catch (Throwable t) {
			LOG.error("[ERROR transaction execute]:{}, args:{}, Error: {}", info, args==null?pjp.getArgs():args, t);
			if(txStatus != null) {
				txManager.rollback(txStatus);
			}
			throw t;
		}
	}
}
