package com.bookmanage.bookmanage.controller;

import com.alibaba.fastjson.JSON;
import com.bookmanage.bookmanage.Response.UploadResponse;
import com.sun.net.httpserver.Authenticator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileController {
  private static final String FILE_PATH = "/file/";
  @RequestMapping("/file")
  public String upload (@RequestParam("file") MultipartFile file) throws IOException {
    UploadResponse response = new UploadResponse();
    // 获取原始名字
    String fileName = file.getOriginalFilename();
    String path = new File("").getCanonicalPath();
    // 文件保存路径
    fileName = path+ fileName;
    // 文件对象
    File dest = new File(fileName);
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
}
