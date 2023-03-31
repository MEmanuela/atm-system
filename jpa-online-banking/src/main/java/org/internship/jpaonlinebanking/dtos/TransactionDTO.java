package org.internship.jpaonlinebanking.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class TransactionDTO {
    private Date date;
    private Double amount;
    private TransactionTypeDTO transactionType;
}
