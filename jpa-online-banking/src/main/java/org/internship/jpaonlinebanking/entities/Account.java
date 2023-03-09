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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Account")
@Data
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Column(name = "ID", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    @Column(name = "Name", unique = true)
    private String name;
    @Column(name = "DateOpened")
    private Date dateOpened;
    @Column(name = "balance")
    private Double balance;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "typeId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AccountType accountType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
//    @OneToMany(mappedBy = "baseAccount", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Transaction> baseAccountTransactions = new ArrayList<Transaction>();
//    @OneToMany(mappedBy = "receivingAccount", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Transaction> receivingAccountTransactions = new ArrayList<Transaction>();
    public User getUser() {
        return user;
    }
    @JsonIgnore
    public void setAccountType(AccountType type) {
        this.accountType = type;
    }
    @JsonIgnore
    public void setUser(User user) {
        this.user = user;
    }
}
