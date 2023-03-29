package org.internship.jpaonlinebanking.controllers;

import jakarta.validation.Valid;
import org.internship.jpaonlinebanking.entities.Account;
import org.internship.jpaonlinebanking.entities.Transaction;
import org.internship.jpaonlinebanking.entities.User;
import org.internship.jpaonlinebanking.exceptions.AuthorizationException;
import org.internship.jpaonlinebanking.exceptions.UserAuthenticationException;
import org.internship.jpaonlinebanking.security.PasswordUpdateRequest;
import org.internship.jpaonlinebanking.services.AccountService;
import org.internship.jpaonlinebanking.services.TransactionService;
import org.internship.jpaonlinebanking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<User> getUserById(@PathVariable(value = "userId") Long userId,
                                               @AuthenticationPrincipal User user) {
        if (user.getUserId() != userId) {
            throw new AuthorizationException("You are not authorized to see user's details");
        }
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }
    @GetMapping("/accounts/{userId}")
    public List<Account> getAccountsByUser(@PathVariable(value = "userId") Long userId,
                                           @AuthenticationPrincipal User user) {
        if (user.getUserId() != userId) {
            throw new AuthorizationException("You are not authorized to see user's accounts");
        }
        return accountService.getAccountsByUser(userId);
    }
    @GetMapping("/transactionHistory/{userId}")
    public List<List<Transaction>> getTransactionsByUser(@PathVariable(value = "userId") Long userId,
                                                         @AuthenticationPrincipal User user) {
        if (user.getUserId() != userId) {
            throw new AuthorizationException("You are not authorized to see user's transaction history");
        }
        return transactionService.getTransactionsByUser(userId);
    }
    @PostMapping("/{userId}/{typeId}/{accountId}/withdraw")
    public Transaction createWithdrawTransaction(@PathVariable(value = "userId") Long userId,
                                                 @PathVariable(value = "typeId") Long typeId,
                                                 @PathVariable(value = "accountId") Long accountId,
                                                 @Valid @RequestBody Transaction transaction,
                                                 @AuthenticationPrincipal User user) {
        if (user.getUserId() != userId) {
            throw new AuthorizationException("You are not authorized to perform this transaction");
        }
        return transactionService.createBasicTransaction(typeId, transaction, accountId);
    }
    @PostMapping("/{userId}/{typeId}/{accountId}/deposit")
    public Transaction createDepositTransaction(@PathVariable(value = "userId") Long userId,
                                                @PathVariable(value = "typeId") Long typeId,
                                                @PathVariable(value = "accountId") Long accountId,
                                                @Valid @RequestBody Transaction transaction,
                                                @AuthenticationPrincipal User user) {
        if (user.getUserId() != userId) {
            throw new AuthorizationException("You are not authorized to perform this transaction");
        }
        return transactionService.createBasicTransaction(typeId, transaction, accountId);
    }
    @PostMapping("/{userId}/{typeId}/{baseAccId}/{recAccId}/transferTransaction")
    public Transaction createTransferTransaction(@PathVariable(value = "userId") Long userId,
                                                 @PathVariable(value = "typeId") Long typeId,
                                                 @PathVariable(value = "baseAccId") Long baseAccId,
                                                 @PathVariable(value = "recAccId") Long recAccId,
                                                 @Valid @RequestBody Transaction transaction,
                                                 @AuthenticationPrincipal User user) {
        if (user.getUserId() != userId) {
            throw new AuthorizationException("You are not authorized to perform this transaction");
        }
        return transactionService.createTransferTransaction(typeId, transaction, baseAccId, recAccId);
    }
    @PutMapping("/{userId}/password")
    public void updatePassword(@PathVariable(value = "userId") Long userId,
                               @Valid @RequestBody PasswordUpdateRequest request,
                               @AuthenticationPrincipal User user) {
        if (user.getUserId() != userId) {
            throw new AuthorizationException("You are not authorized to change user's password");
        }
        userService.updateUserPassword(userId, request.getPassword());
    }
}
