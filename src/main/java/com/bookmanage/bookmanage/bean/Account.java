package com.bookmanage.bookmanage.bean;

import lombok.Data;

@Data
public class Account {
  private Long id;
  private boolean isManager;
  private String name;
  private String password;
  private String sex;
  private Integer age;
  private String address;
}