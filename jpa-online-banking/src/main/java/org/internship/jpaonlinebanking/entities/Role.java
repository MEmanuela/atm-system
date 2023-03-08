package org.internship.jpaonlinebanking.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Role")
@Data
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Column(name = "ID", nullable = false, length = 5)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;
    @Column(name = "Type")
    private String type;
    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users = new ArrayList<User>();
    public Role(Long roleId, String type) {
        this.roleId = roleId;
        this.type = type;
    }
}
