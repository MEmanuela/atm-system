package org.internship.jpaonlinebanking.controllers;

import jakarta.validation.Valid;
import org.internship.jpaonlinebanking.dtos.AccountDTO;
import org.internship.jpaonlinebanking.dtos.TransactionDTO;
import org.internship.jpaonlinebanking.dtos.UserDTO;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v${api-version}")
public class CustomerController {
    @Autowired
    UserService userService;
    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;
    @GetMapping("/user/{userId}")
    @PreAuthorize("#userId == principal.userId")
    public ResponseEntity<UserDTO> getUserById(@PathVariable(value = "userId") Long userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }
    @GetMapping("/accounts/{userId}")
    @PreAuthorize("#userId == principal.userId")
    public ResponseEntity<List<AccountDTO>> getAccountsByUser(@PathVariable(value = "userId") Long userId) {
        return new ResponseEntity<>(accountService.getAccountsByUser(userId), HttpStatus.OK);
    }
    @GetMapping("/transactionHistory/{userId}")
    @PreAuthorize("#userId == principal.userId")
    public ResponseEntity<List<List<TransactionDTO>>> getTransactionsByUser(@PathVariable(value = "userId") Long userId) {
        return new ResponseEntity<>(transactionService.getTransactionsByUser(userId), HttpStatus.OK);
    }
    @PostMapping("/{typeId}/{accountId}/withdraw")
    @PreAuthorize("@restrictAccessService.hasAccount(principal.userId, #accountId)")
    public ResponseEntity createWithdrawTransaction(@PathVariable(value = "typeId") Long typeId,
                                                 @PathVariable(value = "accountId") Long accountId,
                                                 @Valid @RequestBody TransactionDTO transactionDTO) {
        transactionService.createBasicTransaction(typeId, transactionDTO, accountId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PostMapping("/{typeId}/{accountId}/deposit")
    @PreAuthorize("@restrictAccessService.hasAccount(principal.userId, #accountId)")
    public ResponseEntity createDepositTransaction(@PathVariable(value = "typeId") Long typeId,
                                                @PathVariable(value = "accountId") Long accountId,
                                                @Valid @RequestBody TransactionDTO transactionDTO) {
        transactionService.createBasicTransaction(typeId, transactionDTO, accountId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PostMapping("/{typeId}/{baseAccId}/{recAccId}/transferTransaction")
    @PreAuthorize("@restrictAccessService.hasAccount(principal.userId, #baseAccId)")
    public ResponseEntity createTransferTransaction(@PathVariable(value = "typeId") Long typeId,
                                                 @PathVariable(value = "baseAccId") Long baseAccId,
                                                 @PathVariable(value = "recAccId") Long recAccId,
                                                 @Valid @RequestBody TransactionDTO transactionDTO) {
        transactionService.createTransferTransaction(typeId, transactionDTO, baseAccId, recAccId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PutMapping("/{userId}/password")
    @PreAuthorize("#userId == principal.userId")
    public ResponseEntity updatePassword(@PathVariable(value = "userId") Long userId,
                               @Valid @RequestBody PasswordUpdateRequest request) {
        userService.updateUserPassword(userId, request.getPassword());
        return new ResponseEntity(HttpStatus.OK);
    }
}
