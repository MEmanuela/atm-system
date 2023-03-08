package org.internship.jpaonlinebanking;

import org.internship.jpaonlinebanking.entities.*;
import org.internship.jpaonlinebanking.exceptions.TransactionException;
import org.internship.jpaonlinebanking.repositories.AccountRepository;
import org.internship.jpaonlinebanking.repositories.TransactionRepository;
import org.internship.jpaonlinebanking.repositories.TransactionTypeRepository;
import org.internship.jpaonlinebanking.services.TransactionService;
import org.internship.jpaonlinebanking.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionTypeRepository transactionTypeRepository;
    @Mock
    private AccountRepository accountRepository;
    private TransactionService underTest;
    @BeforeEach
    void setUp() {
        underTest = new TransactionService(transactionTypeRepository, transactionRepository, accountRepository);
    }
    @Test
    void canGetAllTransactions() {
        underTest.getAllTransactions();
        verify(transactionRepository).findAll();
    }
    @Test
    void canGetTransactionsByType() {
        //given
        Transaction transaction = new Transaction();
        transaction.setAmount(20.0);
        //when
        when(transactionRepository.findByTransactionType_Id(anyLong()))
                .thenReturn(List.of(transaction));
        var actual = underTest.getTransactionsByType(1l);
        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(List.of(transaction));
        verify(transactionRepository).findByTransactionType_Id(anyLong());
    }
    @Test
    void canGetTransactionsByAccount() {
        //given
        Transaction transaction = new Transaction();
        transaction.setAmount(20.0);
        //when
        when(transactionRepository.findByBaseAccount_AccountId(anyLong()))
                .thenReturn(List.of(transaction));
        var actual = underTest.getTransactionsByAccount(1l);
        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(List.of(transaction));
        verify(transactionRepository).findByBaseAccount_AccountId(anyLong());
    }
    @Test
    void canCreateBasicTransaction() {
        //given
        Account account = new Account();
        account.setName("Test Account");
        account.setBalance(60.0);
        Transaction transaction = new Transaction();
        transaction.setAmount(20.0);
        //when
        when(transactionTypeRepository.findById(anyLong()))
                .thenReturn(Optional.of(new TransactionType(1l, "withdraw")));
        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account));
        underTest.createBasicTransaction(1l, transaction, 1l);
        //then
        ArgumentCaptor<Transaction> transactionArgumentCaptor =
                ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionArgumentCaptor.capture());
        Transaction capturedTransaction = transactionArgumentCaptor.getValue();
        assertThat(capturedTransaction).isEqualTo(transaction);
    }
    @Test
    void withdrawTransactionDeductsMoneyFromAccount() {
        //given
        Account account = new Account();
        account.setName("Test Account");
        account.setBalance(40.0);
        Transaction transaction = new Transaction();
        transaction.setAmount(20.0);
        //when
        when(transactionTypeRepository.findById(1l))
                .thenReturn(Optional.of(new TransactionType(1l, "withdraw")));
        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account));
        when(transactionRepository.findById(anyLong()))
                .thenReturn(Optional.of(transaction));
        underTest.createBasicTransaction(1l, transaction, 1l);
        //then
        assertThat(transactionRepository.findById(anyLong()).get()
                .getBaseAccount().getBalance()).isEqualTo(20.0);
    }
    @Test
    void withdrawTransactionDoesNotHappenIfNotEnoughMoney() {
        //given
        Account account = new Account();
        account.setName("Test Account");
        account.setBalance(10.0);
        Transaction transaction = new Transaction();
        transaction.setAmount(20.0);
        //when
        when(transactionTypeRepository.findById(1l))
                .thenReturn(Optional.of(new TransactionType(1l, "withdraw")));
        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account));

        //then
        assertThrows(TransactionException.class, () -> underTest.createBasicTransaction(1l, transaction, 1l));
    }
    @Test
    void depositTransactionAddsMoneyToAccount() {
        //given
        Account account = new Account();
        account.setName("Test Account");
        account.setBalance(40.0);
        Transaction transaction = new Transaction();
        transaction.setAmount(20.0);
        //when
        when(transactionTypeRepository.findById(2l))
                .thenReturn(Optional.of(new TransactionType(2l, "deposit")));
        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account));
        when(transactionRepository.findById(anyLong()))
                .thenReturn(Optional.of(transaction));
        underTest.createBasicTransaction(2l, transaction, 1l);
        //then
        assertThat(transactionRepository.findById(anyLong()).get()
                .getBaseAccount().getBalance()).isEqualTo(60.0);
    }
    @Test
    void transferDeductsMoneyFromBaseAccAndAddsMoneyToRecAcc() {
        //given
        Account baseAccount = new Account();
        baseAccount.setName("Base Account");
        baseAccount.setBalance(40.0);
        baseAccount.setAccountId(1l);
        Account receivingAccount = new Account();
        receivingAccount.setName("Receiving Account");
        receivingAccount.setBalance(40.0);
        receivingAccount.setAccountId(2l);
        Transaction transaction = new Transaction();
        transaction.setAmount(20.0);
        //when
        when(transactionTypeRepository.findById(3l))
                .thenReturn(Optional.of(new TransactionType(3l, "transfer")));
        when(accountRepository.findById(1l))
                .thenReturn(Optional.of(baseAccount));
        when(accountRepository.findById(2l))
                .thenReturn(Optional.of(receivingAccount));
        when(transactionRepository.findById(anyLong()))
                .thenReturn(Optional.of(transaction));
        underTest.createTransferTransaction(3l, transaction, 1l, 2l);
        //then
        assertThat(transactionRepository.findById(anyLong()).get()
                .getBaseAccount().getBalance()).isEqualTo(20.0);
        assertThat(transactionRepository.findById(anyLong()).get()
                .getReceivingAccount().getBalance()).isEqualTo(60.0);

    }
}
