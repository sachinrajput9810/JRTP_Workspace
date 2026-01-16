package com.jrtp.userMgmt.service;

import com.jrtp.userMgmt.entities.UserAccount;

import java.util.List;

public interface UserAccountService {
     public String saveOrUpdateUserAccount(UserAccount userAccount);
     public List<UserAccount> getAllUserAccounts();
     public UserAccount getUserAccountById(Integer id);
     public boolean deleteUserAccountById(Integer id);
     public boolean updateUserAccountStatus(Integer id , String status);
}
