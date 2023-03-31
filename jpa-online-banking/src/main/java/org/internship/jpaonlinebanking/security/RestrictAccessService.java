package org.internship.jpaonlinebanking.security;

import org.internship.jpaonlinebanking.dtos.AccountDTO;
import org.internship.jpaonlinebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("restrictAccessService")
public class RestrictAccessService {
    @Autowired
    AccountService accountService;
    public boolean hasAccount(Long userId, Long accountId) {
        AccountDTO accountDTO = this.accountService.getAccountById(accountId);
        List<AccountDTO> accountDTOList = this.accountService.getAccountsByUser(userId);
        if (accountDTOList.contains(accountDTO)) return true;
        else return false;
    }
}
