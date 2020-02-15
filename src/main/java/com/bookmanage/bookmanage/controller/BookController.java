package com.bookmanage.bookmanage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bookmanage.bookmanage.Constant;
import com.bookmanage.bookmanage.Response.GetBooksResponse;
import com.bookmanage.bookmanage.bean.Account;
import com.bookmanage.bookmanage.bean.Book;
import com.bookmanage.bookmanage.model.BookModel;
import com.bookmanage.bookmanage.model.BookSearch;
import com.bookmanage.bookmanage.request.SubmitRequest;
import com.bookmanage.bookmanage.utils.FileUtil;
import com.google.common.collect.Lists;
import org.apache.tomcat.jni.Local;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/book")
public class BookController {
  @Autowired
  private BookModel bookModel;
  @PostMapping
  @RequestMapping("/save")
  public JSONObject save(Book book) {
    JSONObject jsonObject = new JSONObject();
    try {
      bookModel.saveBook(book);
      jsonObject.put(Constant.RESULT, Constant.SUCCESS);
    }catch (Throwable e) {
      jsonObject.put(Constant.RESULT, Constant.FAILED);
    }
    return jsonObject;
  }

  @GetMapping
  @RequestMapping("/user/upload")
  public String getBooksByUser(Long userId,String search,Integer offset) {
    GetBooksResponse response = new GetBooksResponse();
    try {
      List<Book> books = bookModel.getBooks(BookSearch.builder().accountId(userId).keyWord(search).build());
      transBookPath(books);
      List<List<Book>> partitions = Lists.partition(books,10);
      if (offset < partitions.size()) {
        response.setBooks(partitions.get(offset));
      }
      response.setBooks(books);
      response.setResult(Constant.SUCCESS);
    }catch (Throwable e){
      response.setResult(Constant.FAILED);
    }
    return JSON.toJSONString(response);
  }

  private void transBookPath(List<Book> books) {
    books.forEach(book -> {
      List<String> absolutePaths = Arrays
              .stream(book.getPath().split(","))
              .map(path -> System.getProperty("user.dir") + FileUtil.FILE_PATH + path)
              .collect(Collectors.toList());
       book.setPath(StringUtils.join(absolutePaths, ','));
    });
    books.forEach(book -> {
              book.setPath(book.getPath().replaceAll("\\\\", "/"));
            }
    );
  }

  @GetMapping
  @RequestMapping("/all")
  public String getAllBooks(String search, Integer offset) {
    GetBooksResponse response = new GetBooksResponse();
    HttpServletRequest request = AccountController.getRequest();
    Account account = (Account)request.getSession().getAttribute("account_info");
    Boolean flag = account.getIsManager() ? null : Boolean.TRUE;
    try {
      List<Book> books = bookModel.getBooks(BookSearch.builder().verifyed(flag).keyWord(search).build());
      transBookPath(books);
      List<List<Book>> partitions = Lists.partition(books,10);
      if (offset < partitions.size()) {
        response.setBooks(partitions.get(offset));
      }
      response.setResult(Constant.SUCCESS);
    }catch (Throwable e){
      response.setResult(Constant.FAILED);
    }
    return JSON.toJSONString(response);
  }

  @PutMapping
  @RequestMapping("/submit")
  public JSONObject submit(SubmitRequest request) {
    JSONObject jsonObject = new JSONObject();
    Book book = Book.builder().build();
    book.setAccountId(request.getUserId());
    book.setAuthorName(request.getAuthorName());
    book.setPath(StringUtils.join(request.getFileList(), ','));
    book.setSource(request.getSource());
    book.setTitle(request.getTittle());
    book.setUserName(request.getUserName());
    book.setPushDate(Long.parseLong(LocalDate.now().toString().replaceAll("-","")));
    try {
      bookModel.saveBook(book);
      jsonObject.put(Constant.RESULT, Constant.SUCCESS);
    }catch (Throwable e) {
      jsonObject.put(Constant.RESULT, Constant.FAILED);
      jsonObject.put(Constant.SUCCESS, e);
    }
    return jsonObject;
  }
  @PutMapping
  @RequestMapping("/audit")
  public JSONObject submit(Long bookId, Integer auditCode) {
    JSONObject jsonObject = new JSONObject();
    Boolean verdifyed = auditCode.equals(1) ? Boolean.TRUE : Boolean.FALSE;
    try {
      bookModel.updateBook(Book.builder().verifyed(verdifyed).id(bookId).build());
      jsonObject.put(Constant.RESULT, Constant.SUCCESS);
    }catch (Throwable e) {
      jsonObject.put(Constant.RESULT, Constant.FAILED);
      jsonObject.put(Constant.SUCCESS, e);
    }
    return jsonObject;
  }
}
