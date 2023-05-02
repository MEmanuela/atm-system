package org.internship.jpaonlinebanking;

import org.internship.jpaonlinebanking.dtos.AccountDTO;
import org.internship.jpaonlinebanking.entities.Account;
import org.internship.jpaonlinebanking.entities.AccountType;
import org.internship.jpaonlinebanking.entities.Role;
import org.internship.jpaonlinebanking.entities.User;
import org.internship.jpaonlinebanking.repositories.AccountRepository;
import org.internship.jpaonlinebanking.repositories.AccountTypeRepository;
import org.internship.jpaonlinebanking.repositories.UserRepository;
import org.internship.jpaonlinebanking.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountTypeRepository accountTypeRepository;
    @Mock
    private MessageSource messageSource;
    private AccountService underTest;
    @BeforeEach
    void setUp() {
        underTest = new AccountService(accountRepository, accountTypeRepository, userRepository, messageSource);
    }
    @Test
    void canCreateAccount() {
        //given
        User user = new User();
        user.setName("John Doe");

        //when
        when(accountTypeRepository.findById(anyLong()))
                .thenReturn(Optional.of(new AccountType(1l, "basic")));
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        underTest.createAccount(1l, 1l);
        //then
        ArgumentCaptor<Account> accountArgumentCaptor =
                ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgumentCaptor.capture());
        Account capturedAccount = accountArgumentCaptor.getValue();
        assertThat(capturedAccount.getName()).isEqualTo("ACC_1_basic_0");
    }
    @Test
    void createdAccountHasInitialBalanceZero() {
        //given
        User user = new User();
        user.setName("John Doe");

        //when
        when(accountTypeRepository.findById(anyLong()))
                .thenReturn(Optional.of(new AccountType(1l, "basic")));
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        underTest.createAccount(1l, 1l);
        //then
        ArgumentCaptor<Account> accountArgumentCaptor =
                ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgumentCaptor.capture());
        Account capturedAccount = accountArgumentCaptor.getValue();
        //then
        assertThat(capturedAccount.getBalance()).isEqualTo(0.0);
    }
    @Test
    void canGetAccountsByUser() {
        //given
        Account account = new Account();
        account.setName("John Doe");
        account.setDateOpened(new Date(2019-12-12));
        //when
        when(accountRepository.findByUser_UserId(anyLong()))
                .thenReturn(List.of(account));
        var actual = underTest.getAccountsByUser(1l);
        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(List.of(account));
        verify(accountRepository).findByUser_UserId(anyLong());
    }
    @Test
    void canDeleteAccountByName() {
        doNothing().when(accountRepository).deleteByName(anyString());

        underTest.deleteAccountByName(anyString());
        verify(accountRepository).deleteByName(anyString());
    }
}
