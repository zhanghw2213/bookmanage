package com.bookmanage.bookmanage.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
  private Long id;
  private Boolean isManager;
  private String name;
  private String password;
  private String sex;
  private Integer age;
  private String address;
}
