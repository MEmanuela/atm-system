package org.internship.jpaonlinebanking.services;

import lombok.AllArgsConstructor;
import org.internship.jpaonlinebanking.entities.Account;
import org.internship.jpaonlinebanking.entities.AccountType;
import org.internship.jpaonlinebanking.entities.User;
import org.internship.jpaonlinebanking.exceptions.ResourceNotFoundException;
import org.internship.jpaonlinebanking.repositories.AccountRepository;
import org.internship.jpaonlinebanking.repositories.AccountTypeRepository;
import org.internship.jpaonlinebanking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class AccountService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountTypeRepository accountTypeRepository;
    @Autowired
    UserRepository userRepository;

    @Transactional
    public Account createAccount(Long typeId, Account account, Long userId) {
        List<Account> accounts = new ArrayList<Account>();
        AccountType type1 = new AccountType();
        User user1 = new User();

        Optional<AccountType> byId = accountTypeRepository.findById(typeId);
        Optional<User> forUser = userRepository.findById(userId);
        if (!byId.isPresent()) {
            throw new ResourceNotFoundException("Account type with id " + typeId + "does not exist");
        } else if (!forUser.isPresent()) {
            throw new ResourceNotFoundException("User with id " + userId + "does not exist");
        }
        AccountType type = byId.get();
        User user = forUser.get();

        // tie Account Type to Account
        account.setAccountType(type);
        // tie User to Account
        account.setUser(user);
        account.setBalance(0.0);
        account.setName(account.getName().replace(" ", ""));

        Account account1 = accountRepository.save(account);

        // tie Account to Account Type
        // tie Account to User
        accounts.add(account1);

//        type1.setAccounts(accounts);
        user1.setAccounts(accounts);

        return account1;
    }

    public List<Account> getAccountsByUser(Long userId) {
        return accountRepository.findByUser_UserId(userId);
    }

    public void deleteAccountById(Long accountId) {
        accountRepository.deleteById(accountId);
    }
    @Transactional
    public void deleteAccountByName(String name) {
        accountRepository.deleteByName(name);
    }
}
