package com.bookmanage.bookmanage.request;

import lombok.Data;

@Data
public class LoginRequest {
  private String username;
  private String password;
}
