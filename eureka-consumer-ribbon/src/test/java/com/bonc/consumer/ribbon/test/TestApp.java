package com.bonc.consumer.ribbon.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import com.bonc.consumer.ribbon.controller.AppTestController;

@SpringBootTest
@TestInstance(value = Lifecycle.PER_CLASS)//每次测试构建新的测试实例，PRE_METHOD每个测试方法
public class TestApp {
	private static final Logger LOG = LoggerFactory.getLogger(TestApp.class);
	@Autowired
	WebApplicationContext context;
	MockMvc mvc;
	/**非私有static void方法；
	 * @TestInstance；标记类，可为实例方法 */
	@BeforeAll
	public void initMock() {
		mvc=webAppContextSetup(context).build();;
	}
	
	/**
	 * MockMvc请求/test，测试项目是否能正常启动。<p>
	 * 判定请求不符合预期；自动抛异常。<p>
	 * [TEST RESULT]:后打印返回结果，info级别日志<p>
	 * */
	@Test
	public void testApp() throws Exception {
		String url="/test";
		String appName=context.getEnvironment()
				.getProperty("spring.application.name", "APP");
		String expectResult=String.format(AppTestController.TEST_TEMP, appName);
		ResultActions result = mvc.perform(get(url))
			.andExpect(status().isOk())
			.andExpect(content().string(expectResult));
		LOG.info("[TEST RESULT]: {}", result.andReturn().getResponse().getContentAsString());
	}
	
	
	
}
