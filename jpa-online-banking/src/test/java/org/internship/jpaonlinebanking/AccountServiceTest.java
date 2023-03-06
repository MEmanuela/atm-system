package org.internship.jpaonlinebanking;

import org.internship.jpaonlinebanking.entities.Account;
import org.internship.jpaonlinebanking.entities.AccountType;
import org.internship.jpaonlinebanking.entities.Role;
import org.internship.jpaonlinebanking.entities.User;
import org.internship.jpaonlinebanking.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AccountServiceTest {
    @Autowired
    private AccountService accountService;
    @Test
    void createdAccountHasInitialBalanceZero() {
        User user = User.builder()
                .userId(Long.valueOf(3)).name("John Doe")
                .phone("0856623956").email("john.doe@gmail.com")
                .personalCodeNumber("9625418852367").role(new Role(Long.valueOf(2), "customer"))
                .accounts(new ArrayList<>()).build();
        Account account = accountService.createAccount(Long.valueOf(1),
                Account.builder().accountId(Long.valueOf(2))
                .name("John Doe").dateOpened(new Date(2015-10-10))
                .accountType(new AccountType()).user(user)
                .baseAccountTransactions(new ArrayList<>())
                .receivingAccountTransactions(new ArrayList<>())
                 .build(), user.getUserId());
        assertThat(account.getBalance().equals(0.0));
    }
}
