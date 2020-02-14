package com.bookmanage.bookmanage.utils;

import java.io.File;
import java.io.IOException;

public class FileUtil {
  public static final String FILE_PATH = "/file/";

  public static File getFile(String fileName) throws IOException {
    String path = System.getProperty("user.dir");
    // 文件保存路径
    fileName = path+ FILE_PATH + fileName;
    // 文件对象
    File dest = new File(fileName);
    return dest;
  }
}
