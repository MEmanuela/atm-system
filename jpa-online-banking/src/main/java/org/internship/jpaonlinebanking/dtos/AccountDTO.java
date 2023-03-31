package org.internship.jpaonlinebanking.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class AccountDTO {
    private String name;
    private Date dateOpened;
    private Double balance;
    private AccountTypeDTO accountType;
}
