package file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.MimetypesFileTypeMap;

import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import cn.hutool.core.util.ZipUtil;

public class CsvTest {
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
	public void testCsvSize() throws IOException {
		String path="C:/Users/Administrator/Desktop/test1.csv";
		File csv = new File(path);
		if(!csv.exists()) {
			csv.createNewFile();
		}
		try(FileOutputStream fos = new FileOutputStream(csv)){
			FileChannel writeChannel = fos.getChannel();
			ByteBuffer buf = ByteBuffer.allocate(1024);
			byte[] bytes = "1,\"1,2\",\"1,2\"\",\"3\"\r\n".getBytes();
			buf.put(bytes);
			int size = 100000;
			int len = bytes.length;
			for (int i = 0; i < size; i++) {
				buf.flip();
				writeChannel.write(buf, csv.length());
				//设置position，重复使用内容
				buf.limit(len);
				buf.position(len);
			}
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
	
	@Test
	public void testZip() throws IOException {
		File zipFile = new File("files.zip");//File.createTempFile("files", ".zip");
		List<File> list = Arrays.asList("C:/Users/Administrator/Desktop/test1.csv", 
				"C:/Users/Administrator/Desktop/nana4828669929693324911.csv", 
				"C:/Users/Administrator/Desktop/测试.xlsx")
				.stream()
				.filter(StringUtils::hasText)
				.map(File::new)
				.collect(Collectors.toList());
		File[] files = new File[list.size()];
		list.toArray(files);
		ZipUtil.zip(zipFile, false, files);
		
	}
	
	@Test
	public void testZos() throws IOException {
		File file = new File("C:/Users/Administrator/Desktop/sqlEntity.zip");
		if(!file.exists()) {
			file.createNewFile();
		}
		List<File> list = Arrays.asList(
//				"C:/Users/Administrator/Desktop/test1.csv", 
//				"C:/Users/Administrator/Desktop/nana4828669929693324911.csv", 
//				"C:/Users/Administrator/Desktop/测试.xlsx",
				"C:/Users/Administrator/Desktop/test.txt"
				)
				.stream()
				.filter(StringUtils::hasText)
				.map(File::new)
				.collect(Collectors.toList());
			
		try(ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file))){
			list.forEach(f->{
				if(f==null) return;
				ZipEntry entry = new ZipEntry(f.getName());
				try(FileInputStream fis = new FileInputStream(f)) {
					zos.putNextEntry(entry);
					byte[] bytes = new byte[4*1024];
					int len = fis.read(bytes);
					while(len > 0) {
						System.out.println(new String(bytes, 0 , len));
						zos.write(bytes, 0 , len);
						len = fis.read(bytes);
					}
					zos.flush();
					zos.closeEntry();
				}catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
		
	}
}
