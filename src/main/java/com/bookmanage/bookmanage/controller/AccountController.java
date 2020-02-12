package com.bookmanage.bookmanage.controller;

import com.bookmanage.bookmanage.bean.Account;
import com.bookmanage.bookmanage.model.AccountModel;
import com.bookmanage.bookmanage.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
  @Autowired
  private AccountModel accountModel;
  @GetMapping("/account/login")
  public Account getAccount(LoginRequest request) {
    return accountModel.getAccount(request);
  }

  public Integer createAccount(Account account) {
    return accountModel.createAccount(account);
  }
}
