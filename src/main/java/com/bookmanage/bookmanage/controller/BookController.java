package com.bookmanage.bookmanage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bookmanage.bookmanage.Constant;
import com.bookmanage.bookmanage.Response.GetBooksResponse;
import com.bookmanage.bookmanage.bean.Book;
import com.bookmanage.bookmanage.model.BookModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/book")
public class BookController {
  @Autowired
  private BookModel bookModel;
    @RequestMapping(value = "/submit", method = RequestMethod.PUT)
    public JSONObject getAccount(String tittle, String authorName, String Source, String fileList) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result",true);
        return jsonObject;
    }

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

  @PostMapping
  @RequestMapping("/user/upload")
  public String getBooksByUser(Long userId) {
    GetBooksResponse response = new GetBooksResponse();
    try {
      List<Book> books = bookModel.getBooks(Book.builder().accountId(userId).build());
      response.setBooks(books);
      response.setResult(Constant.SUCCESS);
    }catch (Throwable e){
      response.setResult(Constant.FAILED);
    }
    return JSON.toJSONString(response);
  }

  @PostMapping
  @RequestMapping("/all")
  public String getAllBooks() {
    GetBooksResponse response = new GetBooksResponse();
    try {
      List<Book> books = bookModel.getBooks(Book.builder().verifyed(Boolean.TRUE).build());
      response.setBooks(books);
      response.setResult(Constant.SUCCESS);
    }catch (Throwable e){
      response.setResult(Constant.FAILED);
    }
    return JSON.toJSONString(response);
  }

  @GetMapping
  @RequestMapping("/keyWord")
  public String getAllBooksByKeyWord(String keyWord) {
    GetBooksResponse response = new GetBooksResponse();
    try {
      List<Book> books = bookModel.getBooksByKeyWord(keyWord);
      response.setBooks(books);
      response.setResult(Constant.SUCCESS);
    }catch (Throwable e){
      response.setResult(Constant.FAILED);
    }
    return JSON.toJSONString(response);
  }

  @PostMapping
  @RequestMapping("/verify")
  public JSONObject verify(Long bookId,Boolean verified) {
    JSONObject jsonObject = new JSONObject();
    try {
      bookModel.updateBook(Book.builder().id(bookId).verifyed(verified).pushDate(System.currentTimeMillis()).build());
      jsonObject.put(Constant.RESULT, Constant.SUCCESS);
    }catch (Throwable e) {
      jsonObject.put(Constant.RESULT, Constant.FAILED);
      jsonObject.put(Constant.ERROR, e);
    }
    return jsonObject;
  }
}
