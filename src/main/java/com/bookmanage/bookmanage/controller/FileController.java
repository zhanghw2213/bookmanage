package com.bookmanage.bookmanage.controller;

import com.alibaba.fastjson.JSON;
import com.bookmanage.bookmanage.Response.UploadResponse;
import com.bookmanage.bookmanage.utils.FileUtil;
import com.sun.net.httpserver.Authenticator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {
  @RequestMapping("/upload")
  public String upload (@RequestParam("file") MultipartFile file) throws IOException {
    UploadResponse response = new UploadResponse();
    // 获取原始名字
    String fileName = file.getOriginalFilename();
    // 文件对象
    File dest = FileUtil.getFile(fileName);
    // 判断路径是否存在，如果不存在则创建
    if(!dest.getParentFile().exists()) {
      dest.getParentFile().mkdirs();
      System.out.println(dest.getParentFile().getAbsolutePath());
    }
    try {
      // 保存到服务器中
      file.transferTo(dest);
      response.setFileName(fileName);
      response.setResult("success");
      return JSON.toJSONString(response);
    } catch (Exception e) {
      e.printStackTrace();
    }
    response.setResult("faild");
    return JSON.toJSONString(response);
  }

  @RequestMapping("/download")
  public HttpServletResponse download (String fileName,HttpServletResponse response) throws IOException {
    File dest = FileUtil.getFile(fileName);
    try (InputStream fis = new BufferedInputStream(new FileInputStream(dest.getAbsoluteFile()));
         OutputStream toClient = new BufferedOutputStream(response.getOutputStream())){
      // path是指欲下载的文件的路径。
      // 取得文件名。
      String filename = dest.getName();
      // 以流的形式下载文件。
      byte[] buffer = new byte[fis.available()];
      fis.read(buffer);
      fis.close();
      // 清空response
      response.reset();
      // 设置response的Header
      response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
      response.addHeader("Content-Length", "" + dest.length());

      response.setContentType("application/octet-stream");
      toClient.write(buffer);
      toClient.flush();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return response;
  }
}
