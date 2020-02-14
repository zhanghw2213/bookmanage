package com.bookmanage.bookmanage.model;

import com.bookmanage.bookmanage.bean.Account;
import com.bookmanage.bookmanage.request.LoginRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AccountModel {

  public Account getAccount(Account account);

  @Insert("insert into t_account (is_manager,name,password,sex,age,address) value (#{isManager},#{name},#{password},#{sex},#{age},#{address})")
  public int createAccount(Account account);

  public void updateAccount(Account account);
}
