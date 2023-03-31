package org.internship.jpaonlinebanking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.internship.jpaonlinebanking.dtos.TransactionDTO;
import org.internship.jpaonlinebanking.repositories.UserRepository;
import org.internship.jpaonlinebanking.security.AuthenticationRequest;
import org.internship.jpaonlinebanking.security.AuthenticationService;
import org.internship.jpaonlinebanking.security.PasswordUpdateRequest;
import org.internship.jpaonlinebanking.services.AccountService;
import org.internship.jpaonlinebanking.services.TransactionService;
import org.internship.jpaonlinebanking.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("classpath:application-test.properties")
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
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserRepository userRepository;
    String token;
    @BeforeAll
    void setUp() {
        AuthenticationRequest request = new AuthenticationRequest("alex_smith","it68al");
        token = authenticationService.authenticate(request).getToken();
    }
    @Test
    void getUserReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/user/{userId}", 1))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void getUserReturnsOkWithToken() throws Exception {
        mockMvc.perform(get("/api/v1/user/{userId}", 3)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
    @Test
    void getUserReturnsTheUser() throws Exception {
        mockMvc.perform(get("/api/v1/user/{userId}", 3)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }
    @Test
    void getAccountReturnsOkWithToken() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/{userId}", 3)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
    @Test
    void createDepositTransactionAddsTransactionToDb() throws Exception{
        TransactionDTO dto = new TransactionDTO();
        dto.setAmount(10.50);

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/{typeId}/{accountId}/deposit", 2, 5)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertThat(transactionService.getTransactionsByUser(3l).equals(dto));
    }
    @Test
    void createTransferTransactionAddsTransactionToDb() throws Exception{
        TransactionDTO dto = new TransactionDTO();
        dto.setAmount(0.6);

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/v1/{typeId}/{baseAccId}/{recAccId}/transferTransaction",3, 5, 6)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertThat(transactionService.getTransactionsByUser(3l).equals(dto));
    }
    @Test
    void testUpdatePassword() throws Exception {
        PasswordUpdateRequest pass = new PasswordUpdateRequest();
        pass.setPassword("ith868Ale#");

        String newPassword = objectMapper.writeValueAsString(pass);

        mockMvc.perform(put("/api/v1/{userId}/password", 3)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(newPassword).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertThat(userRepository.findById(3l).get().getPassword().equals(passwordEncoder.encode(newPassword)));
    }
}
