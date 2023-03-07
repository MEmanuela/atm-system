package org.internship.jpaonlinebanking.controllers;

import org.internship.jpaonlinebanking.entities.Account;
import org.internship.jpaonlinebanking.entities.Transaction;
import org.internship.jpaonlinebanking.entities.User;
import org.internship.jpaonlinebanking.services.AccountService;
import org.internship.jpaonlinebanking.services.TransactionService;
import org.internship.jpaonlinebanking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {
    @Autowired
    UserService userService;
    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;
    @GetMapping("/user/{userId}")
    public Optional<User> getUserById(@PathVariable(value = "userId") Long userId,
                                      @AuthenticationPrincipal User user) {
        if (user.getUserId() != userId) {
            throw new RuntimeException("Don't have access");
        }
        return userService.getUserById(userId);
    }
    @GetMapping("/accounts/{userId}")
    public List<Account> getAccountsByUser(@PathVariable(value = "userId") Long userId,
                                           @AuthenticationPrincipal User user) {
        if (user.getUserId() != userId) {
            throw new RuntimeException("Don't have access");
        }
        return accountService.getAccountsByUser(userId);
    }
    @GetMapping("/transactionHistory/{userId}")
    public List<List<Transaction>> getTransactionsByUser(@PathVariable(value = "userId") Long userId,
                                                         @AuthenticationPrincipal User user) {
        if (user.getUserId() != userId) {
            throw new RuntimeException("Don't have access");
        }
        return transactionService.getTransactionsByUser(userId);
    }
    @PostMapping("/{userId}/{typeId}/{accountId}/withdraw")
    public Transaction withdrawTransaction(@PathVariable(value = "userId") Long userId,
                                              @PathVariable(value = "typeId") Long typeId,
                                              @PathVariable(value = "accountId") Long accountId,
                                              @RequestBody Transaction transaction,
                                              @AuthenticationPrincipal User user) {
        if (user.getUserId() != userId) {
            throw new RuntimeException("Don't have access");
        }
        return transactionService.createBasicTransaction(typeId, transaction, accountId);
    }
    @PostMapping("/{userId}/{typeId}/{accountId}/deposit")
    public Transaction depositTransaction(@PathVariable(value = "userId") Long userId,
                                              @PathVariable(value = "typeId") Long typeId,
                                              @PathVariable(value = "accountId") Long accountId,
                                              @RequestBody Transaction transaction,
                                              @AuthenticationPrincipal User user) {
        if (user.getUserId() != userId) {
            throw new RuntimeException("Don't have access");
        }
        return transactionService.createBasicTransaction(typeId, transaction, accountId);
    }
    @PostMapping("/{userId}/{typeId}/{baseAccId}/{recAccId}/transferTransaction")
    public Transaction createTransferTransaction(@PathVariable(value = "userId") Long userId,
                                                 @PathVariable(value = "typeId") Long typeId,
                                                 @PathVariable(value = "baseAccId") Long baseAccId,
                                                 @PathVariable(value = "recAccId") Long recAccId,
                                                 @RequestBody Transaction transaction,
                                                 @AuthenticationPrincipal User user) {
        if (user.getUserId() != userId) {
            throw new RuntimeException("Don't have access");
        }
        return transactionService.createTransferTransaction(typeId, transaction, baseAccId, recAccId);
    }
    @PutMapping("/{userId}/password")
    public void updatePassword(@PathVariable(value = "userId") Long userId,
                               @RequestBody String password,
                               @AuthenticationPrincipal User user) {
        if (user.getUserId() != userId) {
            throw new RuntimeException("Don't have access");
        }
        userService.updateUserPassword(userId, password);
    }
}
