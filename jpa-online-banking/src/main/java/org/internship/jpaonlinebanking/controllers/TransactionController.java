package org.internship.jpaonlinebanking.controllers;

import org.internship.jpaonlinebanking.entities.Transaction;
import org.internship.jpaonlinebanking.entities.TransactionType;
import org.internship.jpaonlinebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransactionController {
    @Autowired
    TransactionService transactionService;
    @GetMapping("/getAllTransactionTypes")
    public List<TransactionType> getTransactionTypes() {
        return transactionService.getTypes();
    }
    @GetMapping("/getAllTransactions")
    public List<Transaction> getTransactions() {
        return transactionService.getAllTransactions();
    }
    @GetMapping("/{typeId}/transactions")
    public List<Transaction> getTransactionsByType(@PathVariable(value = "typeId") Long typeId) {
        return transactionService.getTransactionsByType(typeId);
    }
    @GetMapping("/transactions/{accountId}")
    public List<Transaction> getTransactionsByAccount(@PathVariable(value = "accountId") Long accountId) {
        return transactionService.getTransactionsByAccount(accountId);
    }
    @GetMapping("/transactionHistory/{userId}")
    public List<List<Transaction>> getTransactionsByUser(@PathVariable(value = "userId") Long userId) {
        return transactionService.getTransactionsByUser(userId);
    }
    @PostMapping("/{typeId}/{accountId}/basicTransaction")
    public Transaction createBasicTransaction(@PathVariable(value = "typeId") Long typeId,
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
}
