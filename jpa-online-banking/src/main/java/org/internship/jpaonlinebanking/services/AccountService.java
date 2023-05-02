package org.internship.jpaonlinebanking.services;

import lombok.AllArgsConstructor;
import org.internship.jpaonlinebanking.dtos.AccountDTO;
import org.internship.jpaonlinebanking.dtos.UserDTO;
import org.internship.jpaonlinebanking.entities.Account;
import org.internship.jpaonlinebanking.entities.AccountType;
import org.internship.jpaonlinebanking.entities.User;
import org.internship.jpaonlinebanking.exceptions.ResourceNotFoundException;
import org.internship.jpaonlinebanking.mappers.AccountMapper;
import org.internship.jpaonlinebanking.mappers.UserMapper;
import org.internship.jpaonlinebanking.repositories.AccountRepository;
import org.internship.jpaonlinebanking.repositories.AccountTypeRepository;
import org.internship.jpaonlinebanking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
    @Autowired
    private MessageSource messageSource;

    private short countUsersAccounts(Long userId) {
        return accountRepository.countByUser_UserId(userId);
    }

    private String generateAccountName(Long userId, String type) {
        short accId = countUsersAccounts(userId);
        return "ACC_" + userId + "_" + type + "_" + accId;
    }

    @Transactional
    public void createAccount(Long typeId, Long userId) {
        Optional<AccountType> byId = accountTypeRepository.findById(typeId);
        Optional<User> forUser = userRepository.findById(userId);
        if (!byId.isPresent()) {
            throw new ResourceNotFoundException(messageSource.getMessage("exception.resourceNotFound.noSuchType", null, Locale.ENGLISH));
        } else if (!forUser.isPresent()) {
            throw new ResourceNotFoundException(messageSource.getMessage("exception.resourceNotFound.noSuchUser", null, Locale.ENGLISH));
        }
        AccountType type = byId.get();
        User user = forUser.get();

        Account account = new Account();

        // tie Account Type to Account
        account.setAccountType(type);
        // tie User to Account
        account.setUser(user);
        account.setBalance(0.0);
        account.setDateOpened(new Date());
        account.setName(generateAccountName(userId, type.getType()));

        accountRepository.save(account);
    }

    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return AccountMapper.INSTANCE.toListOfAccountDTOs(accounts);
    }

    public List<AccountDTO> getAccountsByUser(Long userId) {
        List<Account> accounts = accountRepository.findByUser_UserId(userId);
        return AccountMapper.INSTANCE.toListOfAccountDTOs(accounts);
    }

    public AccountDTO getAccountById(Long accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (!account.isPresent()) {
            throw new ResourceNotFoundException(messageSource.getMessage("exception.resourceNotFound.noSuchAccount", null, Locale.ENGLISH));
        }
        return AccountMapper.INSTANCE.toAccountDTO(account.get());
    }

    public AccountDTO getAccountByName(String name) {
        Optional<Account> account = accountRepository.findByName(name);
        if (!account.isPresent()) {
            throw new ResourceNotFoundException(messageSource.getMessage("exception.resourceNotFound.noSuchAccount", null, Locale.ENGLISH));
        }
        return AccountMapper.INSTANCE.toAccountDTO(account.get());
    }

    @Transactional
    public void deleteAccountByName(String name) {
        accountRepository.deleteByName(name);
    }
}
