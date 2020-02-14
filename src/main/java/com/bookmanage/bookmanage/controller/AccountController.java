package com.bookmanage.bookmanage.controller;

import com.bookmanage.bookmanage.bean.Account;
import com.bookmanage.bookmanage.model.AccountModel;
import com.bookmanage.bookmanage.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class AccountController {
  @Autowired
  private AccountModel accountModel;
  @RequestMapping(value = "/login", method = RequestMethod.PUT)
  public String getAccount(LoginRequest request) {

    return "index";
  }

  @PostMapping("account/create")
  public Integer createAccount(Account account) {
    return accountModel.createAccount(account);
  }
}
