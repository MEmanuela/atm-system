package org.internship.jpaonlinebanking.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Table(name = "Transaction")
@Data
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Column(name = "ID", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "date")
    private Date date;
    @Column(name = "amount")
    private Double amount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "baseAccountId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Account baseAccount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receivingAccountId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Account receivingAccount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "typeId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TransactionType transactionType;

    public TransactionType getTransactionType() {
        return transactionType;
    }
    public Account getBaseAccount() {
        return baseAccount;
    }
    public Account getReceivingAccount() {
        return receivingAccount;
    }
    @JsonIgnore
    public void setBaseAccount(Account baseAccount) {
        this.baseAccount = baseAccount;
    }
    @JsonIgnore
    public void setReceivingAccount(Account receivingAccount) {
        this.receivingAccount = receivingAccount;
    }
    @JsonIgnore
    public void setTransactionType(TransactionType type) {
        this.transactionType = type;
    }
}
