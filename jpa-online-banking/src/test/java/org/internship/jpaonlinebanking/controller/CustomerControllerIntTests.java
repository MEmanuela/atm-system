package org.internship.jpaonlinebanking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.internship.jpaonlinebanking.entities.Transaction;
import org.internship.jpaonlinebanking.security.AuthenticationRequest;
import org.internship.jpaonlinebanking.security.AuthenticationService;
import org.internship.jpaonlinebanking.services.TransactionService;
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
public class CustomerControllerIntTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void getUserReturnsForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/user/{userId}", 1))
                .andExpect(status().isForbidden());
    }
    @Test
    void getUserReturnsOkWithToken() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("alex_smith","it68al");
        String token = authenticationService.authenticate(request).getToken();
        mockMvc.perform(get("/api/v1/user/{userId}", 4)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
    @Test
    void getUserReturnsTheUser() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("alex_smith","it68al");
        String token = authenticationService.authenticate(request).getToken();
        mockMvc.perform(get("/api/v1/user/{userId}", 4)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }
    @Test
    void getAccountReturnsOkWithToken() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("alex_smith","it68al");
        String token = authenticationService.authenticate(request).getToken();
        mockMvc.perform(get("/api/v1/accounts/{userId}", 4)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
    @Test
    void createWithdrawTransactionAddsTransactionToDb() throws Exception{
        Transaction transaction = new Transaction();
        transaction.setDate(new Date(2019-12-12));
        transaction.setAmount(10.50);

        String json = objectMapper.writeValueAsString(transaction);
        AuthenticationRequest request = new AuthenticationRequest("alex_smith","it68al");
        String token = authenticationService.authenticate(request).getToken();
        mockMvc.perform(post("/api/v1/{userId}/{typeId}/{accountId}/withdraw",
                        4, 1, 3)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertThat(transactionService.getTransactionsByUser(4l).equals(transaction));
    }
    @Test
    void createTransferTransactionAddsTransactionToDb() throws Exception{
        Transaction transaction = new Transaction();
        transaction.setDate(new Date(2019-12-12));
        transaction.setAmount(10.50);

        String json = objectMapper.writeValueAsString(transaction);
        AuthenticationRequest request = new AuthenticationRequest("alex_smith","it68al");
        String token = authenticationService.authenticate(request).getToken();
        mockMvc.perform(post("/api/v1/{userId}/{typeId}/{baseAccId}/{recAccId}/transferTransaction",
                        4, 3, 3, 3)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertThat(transactionService.getTransactionsByUser(4l).equals(transaction));
    }
    @Test
    void testUpdatePassword() throws Exception {
        String newPassword = "password";
        AuthenticationRequest request = new AuthenticationRequest("alex_smith","it68al");
        String token = authenticationService.authenticate(request).getToken();
        mockMvc.perform(put("/api/v1/{userId}/password", 4)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(newPassword).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertThat(userService.getUserById(4l).get().getPassword().equals(passwordEncoder.encode(newPassword)));
    }
}
