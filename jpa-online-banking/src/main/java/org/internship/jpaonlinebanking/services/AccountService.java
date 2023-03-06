package org.internship.jpaonlinebanking.services;

import org.internship.jpaonlinebanking.entities.Account;
import org.internship.jpaonlinebanking.entities.AccountType;
import org.internship.jpaonlinebanking.entities.User;
import org.internship.jpaonlinebanking.exceptions.ResourceNotFoundException;
import org.internship.jpaonlinebanking.repositories.AccountRepository;
import org.internship.jpaonlinebanking.repositories.AccountTypeRepository;
import org.internship.jpaonlinebanking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountTypeRepository accountTypeRepository;
    @Autowired
    UserRepository userRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new ResourceNotFoundException("Account with id " + accountId + " not found");
        }
        return accountRepository.findById(accountId);
    }

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

        Account account1 = accountRepository.save(account);

        // tie Account to Account Type
        // tie Account to User
        accounts.add(account1);

//        type1.setAccounts(accounts);
        user1.setAccounts(accounts);

        return account1;
    }

    public List<Account> getAccountsByType(Long typeId) {
        return accountRepository.findByAccountType_Id(typeId);
    }

    public List<Account> getAccountsByUser(Long userId) {
        return accountRepository.findByUser_UserId(userId);
    }

    public void deleteAccountById(Long accountId) {
        accountRepository.deleteById(accountId);
    }

    public List<AccountType> getTypes() {
        return accountTypeRepository.findAll();
    }

    public AccountType createAccountType(AccountType type) {
        return accountTypeRepository.save(type);
    }
}
