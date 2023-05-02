package org.internship.jpaonlinebanking.controllers;

import jakarta.validation.Valid;
import org.internship.jpaonlinebanking.dtos.UserDTO;
import org.internship.jpaonlinebanking.entities.User;
import org.internship.jpaonlinebanking.security.PasswordUpdateRequest;
import org.internship.jpaonlinebanking.services.AccountService;
import org.internship.jpaonlinebanking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/v${api-version}")
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    AccountService accountService;
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDTO>> getUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/{roleId}/user")
    public ResponseEntity createUser(@PathVariable(value = "roleId") Long roleId,
                                             @Valid @RequestBody UserDTO userDTO) {
        userService.createUser(roleId, userDTO);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity deleteUser(@PathVariable(value = "userId") Long userId) {
        // comment from main
        // comment from conflict-test
        userService.deleteUserById(userId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{userId}/{typeId}/account")
    public ResponseEntity createAccount(@PathVariable(value = "typeId") Long typeId,
                                        @PathVariable(value = "userId") Long userId) {
        accountService.createAccount(typeId, userId);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/account/{accountName}")
    public ResponseEntity deleteAccount(@PathVariable(value = "accountName") String name) {
        accountService.deleteAccountByName(name);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{userId}/password")
    @PreAuthorize("#userId == principal.userId")
    public ResponseEntity updatePassword(@PathVariable(value = "userId") Long userId,
                               @Valid @RequestBody PasswordUpdateRequest request) {
        userService.updateUserPassword(userId, request.getPassword());
        return new ResponseEntity(HttpStatus.OK);
    }
}
