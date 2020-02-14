package com.bookmanage.bookmanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.bookmanage.bookmanage.Constant;
import com.bookmanage.bookmanage.bean.Book;
import com.bookmanage.bookmanage.model.BookModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/book")
public class BookController {
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
}
