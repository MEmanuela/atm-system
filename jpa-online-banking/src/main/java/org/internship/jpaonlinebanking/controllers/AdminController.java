package org.internship.jpaonlinebanking.controllers;

import org.internship.jpaonlinebanking.entities.Account;
import org.internship.jpaonlinebanking.entities.User;
import org.internship.jpaonlinebanking.services.AccountService;
import org.internship.jpaonlinebanking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/v1")
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    AccountService accountService;
    @GetMapping("/getAllUsers")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }
    @PostMapping("/{roleId}/user")
    public User createUser(@PathVariable(value = "roleId") Long roleId,
                           @RequestBody User user) {
        return userService.createUser(roleId, user);
    }
    @DeleteMapping("/user/{userId}")
    public void deleteUser(@PathVariable(value = "userId") Long userId) {
        userService.deleteUserById(userId);
    }
    @PostMapping("/{userId}/{typeId}/account")
    public Account createAccount(@PathVariable(value = "typeId") Long typeId,
                                 @RequestBody Account account,
                                 @PathVariable(value = "userId") Long userId) {
        return accountService.createAccount(typeId, account, userId);
    }
    @DeleteMapping("/account/{accountName}")
    public void deleteAccount(@PathVariable(value = "accountName") String name) {
        accountService.deleteAccountByName(name);
    }
}
