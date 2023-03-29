package org.internship.jpaonlinebanking.mappers;

import org.internship.jpaonlinebanking.dtos.UserDTO;
import org.internship.jpaonlinebanking.entities.User;

import java.util.function.Function;

public class UserMapper {
    public UserDTO toDto(User entity) {
        UserDTO dto = new UserDTO();
        dto.setName(entity.getName());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        dto.setPersonalCodeNumber(entity.getPersonalCodeNumber());
        return dto;
    }

    public User toEntity(UserDTO dto) {
        User entity = new User();
        entity.setName(dto.getName());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setPersonalCodeNumber(dto.getPersonalCodeNumber());
        return entity;
    }
}
