package com.bonc.service.sql.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.activation.MimetypesFileTypeMap;

import org.junit.jupiter.api.Test;
import org.springframework.util.MimeType;
import org.springframework.util.StringUtils;

public class TestCsv {
	@Test
	public void testMime() {
		String type = new MimetypesFileTypeMap().getContentType("nana.rar");
		System.out.println(type);
	}
	@Test
	public void testCsvFormat() throws IOException {
		String path="C:/Users/Administrator/Desktop/test1.csv";
		System.out.println(new File(path).getName());
		Path p=Paths.get(path);
		List<String> list = Files.readAllLines(p);
		
		System.out.println(list);
		Files.write(p, Arrays.asList("1,\"1,2\",\"1,2\"\",\"3\""));
	}
	@Test
	public void testConn() throws IOException, SQLException {
		Connection conn=DriverManager.getConnection("", "sa", null);
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("");
		ResultSetMetaData meta = rs.getMetaData();
		int num = meta.getColumnCount();
		String[] cols = new String[num];
		for (int i = 0; i < cols.length; i++) {
			cols[i] = meta.getColumnName(i+1);
		}
		
		
	}
	@Test
	public void handleDot() throws IOException, SQLException {
		String str = "1,3,\"a,b\",\"1,\"\"2,3\"";

		CsvHandler.toList(str)
			.stream()
			.forEach(System.out::println);
		Pattern PATTERN = Pattern.compile(",\\s*(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
//		Matcher matcher = PATTERN.matcher(str);
//		while(matcher.find()) {
//			System.out.println(matcher.group());
//		}
		String[] strs = str.split(",\\s*(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		System.out.println(Arrays.toString(strs));
		System.out.println("AAAA".replaceAll("AA", "A"));
		
	}
	/**
	 * SqlEntity、文件内容互转
	 * @author litianlin
	 * @date   2020年4月8日下午5:34:14
	 * @Description TODO
	 */
	public static interface FileHandler {
		
	}
	/**
	 * 处理csv分隔问题
	 * @author litianlin
	 * @date   2020年4月7日下午4:04:17
	 * @Description TODO
	 */
	public static class CsvHandler {
		private static final String COMMA_REG=",\\s*(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
		private static final Pattern PATTERN = Pattern.compile(COMMA_REG);//COMMA_REG
		//读取csv文档；按非引号内逗号分隔为集合
		public static List<String> toList(String origin) {
			List<String> result = Arrays.stream(PATTERN.split(origin))
					.map(CsvHandler::unwrap)
					.collect(Collectors.toList());
			return result;
		}
		//处理csv格式；单个引号替换为两个引号，包裹""
		public static String wrap(String origin) {
			return new StringBuffer("\"")
					.append(origin.replaceAll("\"", "\"\""))
					.append("\"")
					.toString();
		}
		//处理csv格式；单个引号替换为两个引号，包裹""
		public static String unwrap(String wrapped) {
			if(StringUtils.hasLength(wrapped)) {
				if(wrapped.startsWith("\"")) {
					wrapped = wrapped.substring(1, wrapped.length()-1);
				}
				wrapped = wrapped.replaceAll("\"\"", "\"");
			}
			return wrapped;
		}
	}
	public String handleStr(String origin) {
		Pattern p = Pattern.compile("(\"[^\"]*?),([^\"]*?\")");
		Matcher m = p.matcher(origin);
		StringBuffer buf = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(buf, m.group().replace(",", "BBB"));
//			System.out.println(m.group());
		}
//		System.out.println(buf.toString());
		m.appendTail(buf);
		return buf.toString();
	}
	@Test
	public void testStr() {
		  String s="2018-07-11,Banner,俄罗斯方块2018新版(iOS),iOS-俄罗斯方-banner,\"1,151\r\n,686\",1,\"1,319\",58.15,\"123\",0.05,0.03%";
		   Pattern p = Pattern.compile("(\"[^\"]*?),([^\"]*?\")");
		   Matcher m = p.matcher(s);
		   StringBuffer sb=new StringBuffer();
		   while(m.find()){
			   m.appendReplacement(sb,m.group().replace(",", ""));
			   System.out.println(m.group());
		   }
		   m.appendTail(sb);
		   System.out.println(sb);
	}
}
