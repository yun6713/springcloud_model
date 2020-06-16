package com.bonc.service.sql.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

@TestInstance(value = Lifecycle.PER_CLASS)
public class InitData {
	Connection conn;
	@BeforeAll
	public void createDataSource() throws SQLException {
		conn = DriverManager.getConnection(
				"", //url
				"sa",//username
				null);//password
		
	}
	//call CSVWRITE ( 'D:/H2/dbbak/20141013.txt', 'SELECT * FROM MYTABLE');
	//CREATE TABLE MYTABLE AS SELECT * FROM CSVREAD('D:/H2/dbbak/20141013.csv');
//	@Test
	public void initH2() throws IOException, SQLException {
		String loc = "data/initUser";
		
		Path path = new ClassPathResource(loc).getFile().toPath();
		execute(path);
	}

	private void execute(Path path) throws SQLException, IOException {
		Statement stmt = conn.createStatement();
		//读取，加载batch；执行
		Files.lines(path)
			.filter(StringUtils::hasText)
			.forEach(line -> {
				try {
					stmt.addBatch(line);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
		stmt.executeBatch();
	}
}
