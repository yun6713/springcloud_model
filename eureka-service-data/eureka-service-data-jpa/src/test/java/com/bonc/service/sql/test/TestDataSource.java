package com.bonc.service.sql.test;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.h2.jdbc.JdbcStatement;

public class TestDataSource {
	public void testStatement() throws SQLException {
		Connection conn = DriverManager.getConnection("", "", "");
		JdbcStatement h2;
		ReentrantReadWriteLock lock;
		WeakReference wr;
	}
}
