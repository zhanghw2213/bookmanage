package com.bookmanage.bookmanage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookSearch {
  private Long id;
  private Long accountId;
  private String title;
  private String authorName;
  private String source;
  private String path;
  private Boolean verifyed;
  private Long pushDate;
  private String keyWord;
}
