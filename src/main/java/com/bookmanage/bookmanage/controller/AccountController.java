package com.bookmanage.bookmanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.bookmanage.bookmanage.bean.Account;
import com.bookmanage.bookmanage.model.AccountModel;
import com.bookmanage.bookmanage.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class AccountController {

  private final JSONObject jsonObject = new JSONObject();

  @Autowired
  private AccountModel accountModel;

  @RequestMapping(value = "/login", method = RequestMethod.PUT)
  public JSONObject getAccount(String name, String password, boolean remember) {
    Account account = Account.builder().name("zhangsan").password("123").build();
    if (account.getName().equals(name) && account.getPassword().equals(password)){
      jsonObject.put("flag",true);
      HttpSession session = getRequest().getSession();
      session.setAttribute("account_info",account);
    }else
      jsonObject.put("flag",false);
    //记住密码
    if (remember)
      jsonObject.put("token",name+"@@"+password);
    return jsonObject;
  }

  @PostMapping("account/create")
  public Integer createAccount(Account account) {
    return accountModel.createAccount(account);
  }

  private HttpServletRequest getRequest() {
    return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
  }
}
