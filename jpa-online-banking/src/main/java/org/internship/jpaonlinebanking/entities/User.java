package org.internship.jpaonlinebanking.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.internship.jpaonlinebanking.annotations.UserPersonalCodeNrConstraint;
import org.internship.jpaonlinebanking.annotations.UserPhoneConstraint;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "User")
@Data
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Column(name = "ID", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(name = "Name", updatable = false)
    @NotBlank(message = "Name is required")
    @NotNull(message = "Name can not be null")
    private String name;
    @Column(name = "Phone", updatable = false)
    @UserPhoneConstraint
    private String phone;
    @Column(name = "Email", updatable = false)
    @Email(message = "Must be a well-formed email address")
    private String email;
    @Column(name = "PersonalCodeNumber", updatable = false)
    @NotBlank(message = "Personal Code Number can not be null")
    @UserPersonalCodeNrConstraint
    private String personalCodeNumber;
    @Column(name = "Username", updatable = false, unique = true)
    private String username;
    @Column(name = "Password")
    private String password;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roleId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Role role;
//    @OneToMany(mappedBy = "accountId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Account> accounts = new ArrayList<Account>();
    @JsonIgnore
    public Role getRole() {
        return role;
    }
    @JsonIgnore
    public void setRole(Role role) {
        this.role = role;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getType()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
