package org.internship.jpaonlinebanking.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "AccountType")
@Data
@Jacksonized
@NoArgsConstructor
public class AccountType {
    @Column(name = "ID", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "Type")
    private String type;
//    @OneToMany(mappedBy = "accountType", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Account> accounts = new ArrayList<Account>();
}
