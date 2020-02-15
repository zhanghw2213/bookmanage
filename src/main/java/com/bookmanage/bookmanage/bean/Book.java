package com.bookmanage.bookmanage.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
  private Long id;
  private Long accountId;
  private String userName;
  private String title;
  private String authorName;
  private String source;
  private String path;
  private Boolean verifyed;
  private Long pushDate;
}
