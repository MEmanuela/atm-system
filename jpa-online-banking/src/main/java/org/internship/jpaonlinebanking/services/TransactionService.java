package org.internship.jpaonlinebanking.services;

import org.internship.jpaonlinebanking.entities.Account;
import org.internship.jpaonlinebanking.entities.Transaction;
import org.internship.jpaonlinebanking.entities.TransactionType;
import org.internship.jpaonlinebanking.exceptions.ResourceNotFoundException;
import org.internship.jpaonlinebanking.exceptions.TransactionException;
import org.internship.jpaonlinebanking.repositories.AccountRepository;
import org.internship.jpaonlinebanking.repositories.TransactionRepository;
import org.internship.jpaonlinebanking.repositories.TransactionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionTypeRepository transactionTypeRepository;
    @Autowired
    AccountRepository accountRepository;

    public TransactionService(TransactionTypeRepository transactionTypeRepository, TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionTypeRepository = transactionTypeRepository;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
    public Optional<Transaction> getTransactionById(Long transactionId) {
        if (!transactionRepository.existsById(transactionId)) {
            throw new ResourceNotFoundException("Transaction with id " + transactionId + "not found");
        }
        return transactionRepository.findById(transactionId);
    }
    public List<Transaction> getTransactionsByType(Long typeId) {
        return transactionRepository.findByTransactionType_Id(typeId);
    }

    public List<Transaction> getTransactionsByAccount(Long accountId) {
        return transactionRepository.findByBaseAccount_AccountId(accountId);
    }

    public List<TransactionType> getTypes() {
        return transactionTypeRepository.findAll();
    }

    public List<List<Transaction>> getTransactionsByUser(Long userId) {
        List<List<Transaction>> transactions = new ArrayList<>();
        List<Account> userAccounts = accountRepository.findByUser_UserId(userId);
        for (Account account:userAccounts) {
            transactions.add(transactionRepository.findByBaseAccount_AccountId(account.getAccountId()));
        }
        return transactions;
    }

    @Transactional
    public Transaction createBasicTransaction(Long typeId, Transaction transaction, Long accountId) {
        List<Transaction> transactions = new ArrayList<Transaction>();
        TransactionType type1 = new TransactionType();
        Account account1 = new Account();

        Optional<TransactionType> byId = transactionTypeRepository.findById(typeId);
        Optional<Account> forAccount = accountRepository.findById(accountId);
        if (!byId.isPresent()) {
            throw new ResourceNotFoundException("Transaction type with id " + typeId + "does not exist");
        } else if (!forAccount.isPresent()) {
            throw new ResourceNotFoundException("Account with id " + accountId + "does not exist");
        }
        TransactionType type = byId.get();
        Account account = forAccount.get();

        // tie Transaction Type to Transaction
        transaction.setTransactionType(type);
        //tie Account to Transaction
        transaction.setBaseAccount(account);
        transaction.setReceivingAccount(null);
        Double amount = transaction.getAmount();
        if (type.getType().equals("withdraw")) {
            //withdraw
            transaction.getBaseAccount().setBalance(transaction.getBaseAccount().getBalance() - amount);
            if (transaction.getBaseAccount().getBalance() < 0.0) {
                throw new TransactionException("Not enough money to withdraw");
            }
        } else if (type.getType().equals("deposit")){
            //deposit
            transaction.getBaseAccount().setBalance(transaction.getBaseAccount().getBalance() + amount);
        }

        Transaction transaction1 = transactionRepository.save(transaction);

        // tie Transaction to TransactionType
        // tie Transaction to Account
        transactions.add(transaction1);

        //type1.setTransactions(transactions);
        //account1.setBaseAccountTransactions(transactions);

        return transaction1;
    }
    @Transactional
    public Transaction createTransferTransaction(Long typeId, Transaction transaction,
                                                 Long baseAccountId, Long receivingAccountId) {
        List<Transaction> transactions = new ArrayList<Transaction>();
        TransactionType type1 = new TransactionType();
        Account account1 = new Account();

        Optional<TransactionType> byId = transactionTypeRepository.findById(typeId);
        Optional<Account> forBaseAccount = accountRepository.findById(baseAccountId);
        Optional<Account> forReceivingAccount = accountRepository.findById(receivingAccountId);
        if (!byId.isPresent()) {
            throw new ResourceNotFoundException("Transaction type with id " + typeId + "does not exist");
        } else if (!forBaseAccount.isPresent()) {
            throw new ResourceNotFoundException("Base account with id " + baseAccountId + "does not exist");
        } else if (!forReceivingAccount.isPresent()) {
            throw new ResourceNotFoundException("Receiving account with id " + receivingAccountId + "does not exist");
        }
        TransactionType type = byId.get();
        Account baseAccount = forBaseAccount.get();
        Account receivingAccount = forReceivingAccount.get();

        // tie Transaction Type to Transaction
        transaction.setTransactionType(type);
        //tie Accounts to Transaction
        transaction.setBaseAccount(baseAccount);
        transaction.setReceivingAccount(receivingAccount);

        Double amount = transaction.getAmount();
        //withdraw from base account
        transaction.getBaseAccount().setBalance(transaction.getBaseAccount().getBalance() - amount);
        //deposit to receiving account
        transaction.getReceivingAccount().setBalance(transaction.getReceivingAccount().getBalance() + amount);
//        if (transaction.getBaseAccount().getAccountId() == Long.valueOf(1)){
//            throw new RuntimeException("Something happened here");
//        }
        Transaction transaction1 = transactionRepository.save(transaction);

        // tie Transaction to TransactionType
        // tie Transaction to Account
        transactions.add(transaction1);

        //type1.setTransactions(transactions);
        //account1.setBaseAccountTransactions(transactions);
        //account1.setReceivingAccountTransactions(transactions);

        return transaction1;
    }
}
