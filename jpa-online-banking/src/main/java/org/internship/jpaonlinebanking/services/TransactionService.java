package org.internship.jpaonlinebanking.services;

import lombok.AllArgsConstructor;
import org.internship.jpaonlinebanking.dtos.TransactionDTO;
import org.internship.jpaonlinebanking.dtos.TransactionTypeDTO;
import org.internship.jpaonlinebanking.entities.Account;
import org.internship.jpaonlinebanking.entities.Transaction;
import org.internship.jpaonlinebanking.entities.TransactionType;
import org.internship.jpaonlinebanking.exceptions.ResourceNotFoundException;
import org.internship.jpaonlinebanking.exceptions.TransactionException;
import org.internship.jpaonlinebanking.mappers.AccountMapper;
import org.internship.jpaonlinebanking.mappers.TransactionMapper;
import org.internship.jpaonlinebanking.mappers.TransactionTypeMapper;
import org.internship.jpaonlinebanking.repositories.AccountRepository;
import org.internship.jpaonlinebanking.repositories.TransactionRepository;
import org.internship.jpaonlinebanking.repositories.TransactionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionTypeRepository transactionTypeRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MessageSource messageSource;

    public List<TransactionDTO> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return TransactionMapper
                .INSTANCE.toListOfTransactionDTOs(transactions);
    }

    public TransactionDTO getTransactionById(Long transactionId) {
        Optional<Transaction> transaction = transactionRepository.findById(transactionId);
        if (!transaction.isPresent()) {
            throw new ResourceNotFoundException(messageSource.getMessage("exception.resourceNotFound", null, Locale.ENGLISH));
        }
        return TransactionMapper.INSTANCE.toTransactionDTO(transaction.get());
    }
    public List<TransactionDTO> getTransactionsByType(Long typeId) {
        List<Transaction> transactions = transactionRepository.findByTransactionType_Id(typeId);
        return TransactionMapper.INSTANCE.toListOfTransactionDTOs(transactions);
    }

    public List<TransactionDTO> getTransactionsByAccount(Long accountId) {
        List<Transaction> transactions = transactionRepository.findByBaseAccount_AccountId(accountId);
        return TransactionMapper.INSTANCE.toListOfTransactionDTOs(transactions);
    }

    public List<TransactionTypeDTO> getTypes() {
        List<TransactionType> transactionTypes = transactionTypeRepository.findAll();
        return TransactionTypeMapper.INSTANCE
                .toListOfTransactionTypeDTOs(transactionTypes);
    }

    public List<List<TransactionDTO>> getTransactionsByUser(Long userId) {
        List<List<Transaction>> transactions = new ArrayList<>();
        List<Account> userAccounts = accountRepository.findByUser_UserId(userId);
        for (Account account:userAccounts) {
            transactions.add(transactionRepository.findByBaseAccount_AccountId(account.getAccountId()));
        }
        return TransactionMapper.INSTANCE.toListOfListsOfTransactionDTOs(transactions);
    }

    @Transactional
    public void createBasicTransaction(Long typeId, TransactionDTO transactionDTO, Long accountId) {
        Optional<TransactionType> byId = transactionTypeRepository.findById(typeId);
        Optional<Account> forAccount = accountRepository.findById(accountId);
        if (!byId.isPresent()) {
            throw new ResourceNotFoundException(messageSource.getMessage("exception.resourceNotFound.noSuchType", null, Locale.ENGLISH));
        } else if (!forAccount.isPresent()) {
            throw new ResourceNotFoundException(messageSource.getMessage("exception.resourceNotFound.noSuchAccount", null, Locale.ENGLISH));
        }
        TransactionType type = byId.get();
        Account account = forAccount.get();

        Transaction transaction = TransactionMapper.INSTANCE.fromTransactionDTO(transactionDTO);

        // tie Transaction Type to Transaction
        transaction.setTransactionType(type);
        //tie Account to Transaction
        transaction.setBaseAccount(account);
        transaction.setReceivingAccount(null);
        transaction.setDate(new Date());
        Double amount = transaction.getAmount();
        if (type.getType().equals("withdraw")) {
            //withdraw
            transaction.getBaseAccount().setBalance(transaction.getBaseAccount().getBalance() - amount);
            if (transaction.getBaseAccount().getBalance() < 0.0) {
                throw new TransactionException(messageSource.getMessage("exception.transactionNotPossible", null, Locale.ENGLISH));
            }
        } else if (type.getType().equals("deposit")){
            //deposit
            transaction.getBaseAccount().setBalance(transaction.getBaseAccount().getBalance() + amount);
        }
        transactionRepository.save(transaction);
    }
    @Transactional
    public void createTransferTransaction(Long typeId, TransactionDTO transactionDTO,
                                                 Long baseAccountId, Long receivingAccountId) {
        Optional<TransactionType> byId = transactionTypeRepository.findById(typeId);
        Optional<Account> forBaseAccount = accountRepository.findById(baseAccountId);
        Optional<Account> forReceivingAccount = accountRepository.findById(receivingAccountId);
        if (!byId.isPresent()) {
            throw new ResourceNotFoundException(messageSource.getMessage("exception.resourceNotFound.noSuchType", null, Locale.ENGLISH));
        } else if (!forBaseAccount.isPresent() || !forReceivingAccount.isPresent()) {
            throw new ResourceNotFoundException(messageSource.getMessage("exception.resourceNotFound.noSuchAccount", null, Locale.ENGLISH));
        }
        TransactionType type = byId.get();
        Account baseAccount = forBaseAccount.get();
        Account receivingAccount = forReceivingAccount.get();

        Transaction transaction = TransactionMapper.INSTANCE.fromTransactionDTO(transactionDTO);

        // tie Transaction Type to Transaction
        transaction.setTransactionType(type);
        //tie Accounts to Transaction
        transaction.setBaseAccount(baseAccount);
        transaction.setReceivingAccount(receivingAccount);

        transaction.setDate(new Date());

        Double amount = transaction.getAmount();
        //withdraw from base account
        transaction.getBaseAccount().setBalance(transaction.getBaseAccount().getBalance() - amount);
        //deposit to receiving account
        transaction.getReceivingAccount().setBalance(transaction.getReceivingAccount().getBalance() + amount);

        if (transaction.getBaseAccount().getBalance() < 0.0) {
            throw new TransactionException(messageSource.getMessage("exception.transactionNotPossible", null, Locale.ENGLISH));
        }
        transactionRepository.save(transaction);
    }
}
