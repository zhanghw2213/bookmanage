package com.bookmanage.bookmanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.bookmanage.bookmanage.Constant;
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

  @Autowired
  private AccountModel accountModel;

  @RequestMapping(value = "/login", method = RequestMethod.PUT)
  public JSONObject getAccount(String name, String password, boolean remember) {
    JSONObject jsonObject = new JSONObject();
    LoginRequest request = LoginRequest.builder().password(password).username(name).build();
    Account account = accountModel.getAccount(request);
    if (account != null){
      jsonObject.put("flag",true);
      HttpSession session = getRequest().getSession();
      session.setAttribute("account_info",account);
    }else
      jsonObject.put("flag",false);
      jsonObject.put("account", account);
    //记住密码
    if (remember)
      jsonObject.put("token",name+"@@"+password);
    return jsonObject;
  }

  @PostMapping("/create")
  public JSONObject createAccount(Account account) {
    JSONObject jsonObject = new JSONObject();
    try {
      if (accountModel.createAccount(account) > 0) {
        jsonObject.put("flag", true);
      }else {
        jsonObject.put("flag", false);
      }
    }catch (Throwable e) {
      jsonObject.put("flag", false);
    }
    return jsonObject;
  }

  @PostMapping("/reset")
  public JSONObject resetPwd(Long id, String password) {
    JSONObject jsonObject = new JSONObject();
    Account account = Account.builder().id(id).password(password).build();
    try {
      accountModel.updateAccount(account);
      jsonObject.put("message", JSONObject.toJSONString(account));
      jsonObject.put(Constant.RESULT,Constant.SUCCESS);

    }catch (Throwable e) {
      jsonObject.put("result", "faild");
    }
    return jsonObject;
  }

  private HttpServletRequest getRequest() {
    return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
  }
}
