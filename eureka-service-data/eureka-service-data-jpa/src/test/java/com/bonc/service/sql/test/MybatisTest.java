package com.bonc.service.sql.test;

import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 * @date 2020年4月27日
 * DESC: 
 */
public class MybatisTest {
	private static final Logger LOG = LoggerFactory.getLogger(MybatisTest.class);

	public void testDefaultSqlSession() {
		DefaultSqlSession session;
		CachingExecutor executor;
		MybatisAutoConfiguration conf;
	}
}
