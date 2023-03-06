package org.internship.jpaonlinebanking.controllers;

import org.internship.jpaonlinebanking.entities.Account;
import org.internship.jpaonlinebanking.entities.Transaction;
import org.internship.jpaonlinebanking.entities.User;
import org.internship.jpaonlinebanking.services.AccountService;
import org.internship.jpaonlinebanking.services.TransactionService;
import org.internship.jpaonlinebanking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Optional<User> getUserById(@PathVariable(value = "userId") Long userId) {
        return userService.getUserById(userId);
    }
    @GetMapping("/accounts/{userId}")
    public List<Account> getAccountsByUser(@PathVariable(value = "userId") Long userId) {
        return accountService.getAccountsByUser(userId);
    }
    @GetMapping("/transactionHistory/{userId}")
    public List<List<Transaction>> getTransactionsByUser(@PathVariable(value = "userId") Long userId) {
        return transactionService.getTransactionsByUser(userId);
    }
    @PostMapping("/{typeId}/{accountId}/withdraw")
    public Transaction withdrawTransaction(@PathVariable(value = "typeId") Long typeId,
                                              @PathVariable(value = "accountId") Long accountId,
                                              @RequestBody Transaction transaction) {
        return transactionService.createBasicTransaction(typeId, transaction, accountId);
    }
    @PostMapping("/{typeId}/{accountId}/deposit")
    public Transaction depositTransaction(@PathVariable(value = "typeId") Long typeId,
                                              @PathVariable(value = "accountId") Long accountId,
                                              @RequestBody Transaction transaction) {
        return transactionService.createBasicTransaction(typeId, transaction, accountId);
    }
    @PostMapping("/{typeId}/{baseAccId}/{recAccId}/transferTransaction")
    public Transaction createTransferTransaction(@PathVariable(value = "typeId") Long typeId,
                                                 @PathVariable(value = "baseAccId") Long baseAccId,
                                                 @PathVariable(value = "recAccId") Long recAccId,
                                                 @RequestBody Transaction transaction) {
        return transactionService.createTransferTransaction(typeId, transaction, baseAccId, recAccId);
    }
    @PutMapping("/{userId}/password")
    public void updatePassword(@PathVariable(value = "userId") Long userId,
                               @RequestBody String password) {
        userService.updateUserPassword(userId, password);
    }
}
