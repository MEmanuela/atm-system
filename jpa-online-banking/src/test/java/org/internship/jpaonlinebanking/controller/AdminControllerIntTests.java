package org.internship.jpaonlinebanking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.internship.jpaonlinebanking.dtos.AccountDTO;
import org.internship.jpaonlinebanking.dtos.AccountTypeDTO;
import org.internship.jpaonlinebanking.dtos.RoleDTO;
import org.internship.jpaonlinebanking.dtos.UserDTO;
import org.internship.jpaonlinebanking.entities.Account;
import org.internship.jpaonlinebanking.entities.Role;
import org.internship.jpaonlinebanking.entities.User;
import org.internship.jpaonlinebanking.exceptions.ResourceNotFoundException;
import org.internship.jpaonlinebanking.security.AuthenticationRequest;
import org.internship.jpaonlinebanking.security.AuthenticationService;
import org.internship.jpaonlinebanking.services.AccountService;
import org.internship.jpaonlinebanking.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("classpath:application-test.properties")
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
    String token;
    @BeforeAll
    void setUp() {
        AuthenticationRequest request = new AuthenticationRequest("criss_evans","something");
        token = authenticationService.authenticate(request).getToken();
    }
    @Test
    void getAllUsersReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/admin/api/v1/getAllUsers"))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void getAllUsersReturnsOkWithToken() throws Exception {
        mockMvc.perform(get("/admin/api/v1/getAllUsers")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
    @Test
    void createUserReturnsTheUser() throws Exception {
        UserDTO dto = new UserDTO();
        dto.setName("Scarlet Brandy");
        dto.setPhone("0956695231");
        dto.setEmail("scarlet.brandy@gmail.com");
        dto.setPersonalCodeNumber("9539496451785");

        Long uId = Long.valueOf(userService.getAllUsers().size());

        String json = objectMapper.writeValueAsString(dto);
        mockMvc.perform(post("/admin/api/v1/{roleId}/user", 2)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertThat(userService.getUserById(uId).equals(dto));
    }
    @Test
    void createAccountReturnsTheAccount() throws Exception {
        Long aId = Long.valueOf(accountService.getAllAccounts().size());

        mockMvc.perform(post("/admin/api/v1/{userId}/{typeId}/account", 3, 1)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated());
        assertThat(accountService.getAccountById(aId).getBalance().equals(0.0));
        assertThat(accountService.getAccountById(aId).getName().equals("ACC_3_basic_2"));
    }
    @Test
    void testDeleteUser() throws Exception {
        UserDTO dto = new UserDTO();
        dto.setName("Leila Dixon");
        dto.setPhone("0956695231");
        dto.setEmail("leila.dixon@gmail.com");
        dto.setPersonalCodeNumber("9539486341785");

        userService.createUser(2l, dto);
        Long uId = Long.valueOf(userService.getAllUsers().size());

        mockMvc.perform(delete("/admin/api/v1/user/{userId}", uId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(uId));
    }
    @Test
    void testDeleteAccount() throws Exception {
        accountService.createAccount(1l, 3l);

        mockMvc.perform(delete("/admin/api/v1/account/{accountName}", "ACC_3_basic_2")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
        assertThrows(ResourceNotFoundException.class, () -> accountService.getAccountByName("ACC_3_basic_2"));
    }
}
