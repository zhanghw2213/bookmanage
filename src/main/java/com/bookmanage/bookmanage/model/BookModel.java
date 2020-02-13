package com.bookmanage.bookmanage.model;

import com.bookmanage.bookmanage.bean.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BookModel {
  @Select("selelct * from t_book where ")
  public List<Book> getBooks(Book bookSearchCondition);
}
