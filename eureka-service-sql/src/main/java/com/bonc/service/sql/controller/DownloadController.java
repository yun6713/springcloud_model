package com.bonc.service.sql.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
@RequestMapping("/download")
public class DownloadController {

	@RequestMapping(value="/csv")
	public String downloadCsv() throws IOException {
		File tempFile = File.createTempFile("nana", "csv");
		PrintWriter writer = getWriter(tempFile);
		
		return null;
	}
	/**
	 * 设置下载信息
	 * @param file 待下载文件
	 * @return
	 * @throws IOException 
	 */
	private PrintWriter getWriter(File file) throws IOException  {
		HttpServletResponse resp = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		String fileName = file.getName();
		String mimeType = new MimetypesFileTypeMap().getContentType(fileName);

		resp.setHeader("content-type", mimeType);
        resp.setContentType(mimeType);
        resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		resp.setContentLengthLong(file.length());
		
		return resp.getWriter();
	}
}
