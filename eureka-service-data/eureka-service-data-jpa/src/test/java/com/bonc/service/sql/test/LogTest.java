package com.bonc.service.sql.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 * @date 2020年4月26日
 * DESC: 
 */
public class LogTest {
	private static final Logger LOG = LoggerFactory.getLogger(LogTest.class);
	@Test
	public void testLogInfo() {
		Map<String, String> temp=new HashMap<>();
		System.out.println(LOG.isTraceEnabled());
		LOG.trace("{}", temp.put("a", "b"));
		System.out.println(temp);
	}
}
