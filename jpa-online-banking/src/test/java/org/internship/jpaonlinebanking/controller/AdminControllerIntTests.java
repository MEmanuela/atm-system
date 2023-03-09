package org.internship.jpaonlinebanking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.internship.jpaonlinebanking.entities.Account;
import org.internship.jpaonlinebanking.entities.Role;
import org.internship.jpaonlinebanking.entities.User;
import org.internship.jpaonlinebanking.exceptions.ResourceNotFoundException;
import org.internship.jpaonlinebanking.security.AuthenticationRequest;
import org.internship.jpaonlinebanking.security.AuthenticationService;
import org.internship.jpaonlinebanking.services.AccountService;
import org.internship.jpaonlinebanking.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("classpath:application-test.properties")
@SqlGroup({
        @Sql(value = "classpath:empty/reset.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(value = "classpath:init/data.sql", executionPhase = BEFORE_TEST_METHOD)
})
public class AdminControllerIntTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void getAllUsersReturnsForbidden() throws Exception {
        mockMvc.perform(get("/admin/api/v1/getAllUsers"))
                .andExpect(status().isForbidden());
    }
    @Test
    void getAllUsersReturnsOkWithToken() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("criss_evans","something");
        String token = authenticationService.authenticate(request).getToken();
        mockMvc.perform(get("/admin/api/v1/getAllUsers")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
    @Test
    void createUserReturnsTheUser() throws Exception {
        User user = new User();
        user.setName("Scarlet Brandy");
        user.setPhone("0956695231");
        user.setEmail("scarlet.brandy@gmail.com");
        user.setPersonalCodeNumber("9539486851785");
        user.setRole(new Role(2l, "customer"));

        String json = objectMapper.writeValueAsString(user);
        AuthenticationRequest request = new AuthenticationRequest("criss_evans","something");
        String token = authenticationService.authenticate(request).getToken();
        mockMvc.perform(post("/admin/api/v1/{roleId}/user", 2)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertThat(userService.getUserById(6l).get().equals(user));
    }
    @Test
    void createAccountReturnsTheAccount() throws Exception {
        Account account = new Account();
        account.setName("New Account");
        account.setDateOpened(new Date(2019-12-12));
        String json = objectMapper.writeValueAsString(account);

        AuthenticationRequest request = new AuthenticationRequest("criss_evans","something");
        String token = authenticationService.authenticate(request).getToken();
        mockMvc.perform(post("/admin/api/v1/{userId}/{typeId}/account", 3, 1)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertThat(accountService.getAccountById(4l).equals(account));
    }
    @Test
    void testDeleteUser() throws Exception {
        User user = new User();
        user.setName("Scarlet Brandy");
        user.setPhone("0956695231");
        user.setEmail("scarlet.brandy@gmail.com");
        user.setPersonalCodeNumber("9539486851785");
        user.setRole(new Role(2l, "customer"));
        userService.createUser(2l, user);
        AuthenticationRequest request = new AuthenticationRequest("criss_evans","something");
        String token = authenticationService.authenticate(request).getToken();
        mockMvc.perform(delete("/admin/api/v1/user/{userId}", 6)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(6l));
    }
    @Test
    void testDeleteAccount() throws Exception {
        Account account = new Account();
        account.setName("New Account");
        account.setDateOpened(new Date(2019-12-12));
        accountService.createAccount(1l, account, 5l);

        AuthenticationRequest request = new AuthenticationRequest("criss_evans","something");
        String token = authenticationService.authenticate(request).getToken();
        mockMvc.perform(delete("/admin/api/v1/account/{accountName}", "NewAccount")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
        assertThrows(ResourceNotFoundException.class, () -> accountService.getAccountById(4l));
    }
}
