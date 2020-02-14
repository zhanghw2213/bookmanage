package com.bookmanage.bookmanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.bookmanage.bookmanage.Constant;
import com.bookmanage.bookmanage.bean.Book;
import com.bookmanage.bookmanage.model.BookModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/book")
public class BookController {

    @RequestMapping(value = "/submit", method = RequestMethod.PUT)
    public JSONObject getAccount(String tittle, String authorName, String Source, String fileList) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result",true);
        return jsonObject;
    }
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
