package com.bookmanage.bookmanage.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitRequest {
  private Long userId;
  private String tittle;
  private String authorName;
  private String source;
  private List<String> fileList;
}
