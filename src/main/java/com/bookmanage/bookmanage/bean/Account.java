package com.bookmanage.bookmanage.bean;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Account {
  private Long id;
  private Boolean isManager;
  private String name;
  private String password;
  private String sex;
  private Integer age;
  private String address;
}
