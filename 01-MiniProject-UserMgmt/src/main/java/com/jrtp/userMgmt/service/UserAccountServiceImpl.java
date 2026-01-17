package com.jrtp.userMgmt.service;

import com.jrtp.userMgmt.entities.UserAccount;
import com.jrtp.userMgmt.repository.UserAccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private UserAccountRepo userAccountRepo;

    @Override
    public String saveOrUpdateUserAccount(UserAccount userAccount) {
        Integer userId = userAccount.getUserId();

        // upsert operation
        userAccountRepo.save(userAccount);

        if (userId == null) {
            return "User account created";
        } else {
            return "User account updated";
        }
    }

    @Override
    public List<UserAccount> getAllUserAccounts() {
        return userAccountRepo.findAll();
    }

    @Override
    public UserAccount getUserAccountById(Integer id) {
        Optional<UserAccount> userAccount = userAccountRepo.findById(id);
        return userAccount.orElse(null);
    }

    @Override
    public boolean deleteUserAccountById(Integer id) {
        Optional<UserAccount> userAccount = userAccountRepo.findById(id);
        if (userAccount.isPresent()) {
            userAccountRepo.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUserAccountStatus(Integer id, String status) {
        try {
            userAccountRepo.findById(id).ifPresent(userAccount -> {
                userAccount.setActiveSw(status);
                userAccountRepo.save(userAccount);
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
