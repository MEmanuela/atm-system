package org.internship.jpaonlinebanking;

import org.internship.jpaonlinebanking.entities.*;
import org.internship.jpaonlinebanking.services.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransactionServiceTest {
    @Autowired
    private TransactionService transactionService;
    @Test
    void withdrawTransactionDeductsMoneyFromAccount() {
    }
    @Test
    void withdrawTransactionAddsMoneyToAccount(){
    }
    @Test
    void transferDeductsMoneyFromBaseAccAndAddsMoneyToRecAcc() {
    }
}
