package com.jrtp.userMgmt.repository;

import com.jrtp.userMgmt.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepo extends JpaRepository<UserAccount, Integer> {
    
}
