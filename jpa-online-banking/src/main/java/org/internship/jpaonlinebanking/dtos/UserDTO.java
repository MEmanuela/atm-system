package org.internship.jpaonlinebanking.dtos;

import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private String phone;
    private String email;
    private String personalCodeNumber;
    private RoleDTO role;
}
