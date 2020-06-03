package com.bonc.service.sql.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestH2 {
	private static final Logger LOG = LoggerFactory.getLogger(TestH2.class);

	@Test
	public void testSQLException() {
		try {
			Connection conn = DriverManager.getConnection(
					"jdbc:h2:./h2", //url
					"sa",//username
					null);
			conn.createStatement().execute("select ltl from nana");
		} catch (SQLException e) {
			LOG.error("error msg: {}, {}", e.getMessage(), e.getStackTrace());
		}//password
	}
}
