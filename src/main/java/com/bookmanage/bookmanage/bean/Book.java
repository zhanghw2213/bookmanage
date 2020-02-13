package com.bookmanage.bookmanage.bean;

import lombok.Data;

@Data
public class Book {
  private Long id;
  private Long accountId;
  private String tittle;
  private String authorName;
  private String Source;
  private String path;
  private Boolean verified;
  private Long pushDate;
}
