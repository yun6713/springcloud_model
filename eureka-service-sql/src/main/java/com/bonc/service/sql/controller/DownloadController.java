package com.bonc.service.sql.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * swagger不支持文件数组上传，上传文件时，文件变量不声明swagger参数。
 * @author litianlin
 * @date   2020年4月14日上午10:59:03
 * @Description TODO
 */
@Api(tags="文件上传下载")
@RestController
public class DownloadController {

	@ApiOperation(value="下载csv", produces="text/csv")
	@RequestMapping(value="/downloadCsv", method=RequestMethod.POST)
	public String downloadCsv() throws IOException{
		PrintWriter writer = null;
		try{
			writer = getWriter("nana.csv");
			//待下载信息
			List<String> list = Arrays.asList("1,2,3","1,2,3","1,2,3");
			for (int i = 0; i < list.size(); i++) {
				writer.println(list.get(i));
			}
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(writer!=null) {
				writer.close();
			}
		}
		return null;
	}
	/**
	 * 设置mimeType、文件名
	 * @param fileName 待下载文件文件名
	 * @return
	 * @throws IOException
	 */
	private PrintWriter getWriter(String fileName) throws IOException  {
		HttpServletResponse resp = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		String mimeType = new MimetypesFileTypeMap().getContentType(fileName);

		resp.setHeader("content-type", mimeType);
        resp.setContentType(mimeType);
        resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		
		return resp.getWriter();
	}
	/**
	 * 设置mimeType、文件名
	 * @param fileName 待下载文件文件名
	 * @return
	 * @throws IOException
	 */
	private OutputStream getOutputStream(String fileName) throws IOException  {
		HttpServletResponse resp = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		String mimeType = new MimetypesFileTypeMap().getContentType(fileName);
		resp.setHeader("content-type", mimeType);
        resp.setContentType(mimeType);
        resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        resp.setCharacterEncoding("UTF-8");
        return resp.getOutputStream();
	}
	
	@ApiOperation(value="上传csv")
	@RequestMapping(value="/uploadCsv", method=RequestMethod.POST)
	public String uploadCsv(@RequestParam("file") MultipartFile file, @RequestParam("file2") MultipartFile file2) throws IOException{
		printFile(file);
		printFile(file2);
		return "ok";
	}
	private void printFile(MultipartFile file) throws IOException {
		System.out.println(file.getName());
		try(	InputStream is = file.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				){
			CharBuffer cbuf = CharBuffer.allocate(8*1024);
			
			int len = isr.read(cbuf);
			StringBuffer sbuf = new StringBuffer();
			while(len > 0) {
				cbuf.flip();
				if(cbuf.hasArray()) {
					sbuf.append(cbuf.array());
				}
				cbuf.clear();
				len = isr.read(cbuf);			
			}
			System.out.println(sbuf.toString());
		}
	}
	@ApiOperation(value="下载zip, 必须配置@ApiOperation.produces", produces="application/octet-stream")
	@RequestMapping(value="/downloadZip", method=RequestMethod.POST)
	public String downloadZip() throws IOException{
		List<File> list = Arrays.asList(
				"C:/Users/Administrator/Desktop/test1.csv", 
				"C:/Users/Administrator/Desktop/测试.xlsx",
				"C:/Users/Administrator/Desktop/test.txt"
				)
				.stream()
				.filter(StringUtils::hasText)
				.map(File::new)
				.collect(Collectors.toList());
		try(ZipOutputStream zos = new ZipOutputStream(getOutputStream("sqlEntity.zip"))){
			for (int i = 0; i < list.size(); i++) {
				File f = list.get(i);
				if(f==null) continue;
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
					zos.closeEntry();
				}catch (IOException e) {
					e.printStackTrace();
				}
			}
			zos.flush();
			zos.close();
			
		}
		
		return null;
	}
	
	
}
