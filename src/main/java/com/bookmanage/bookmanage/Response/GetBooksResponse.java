package com.bookmanage.bookmanage.Response;

import com.bookmanage.bookmanage.bean.Book;
import lombok.Data;

import java.util.List;

@Data
public class GetBooksResponse {
  private List<Book> books;
  private String result;
}
