package org.internship.jpaonlinebanking.mappers;

import org.internship.jpaonlinebanking.dtos.AccountTypeDTO;
import org.internship.jpaonlinebanking.dtos.RoleDTO;
import org.internship.jpaonlinebanking.entities.AccountType;
import org.internship.jpaonlinebanking.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);
    Role fromRoleDTO(RoleDTO dto);
    RoleDTO toRoleDTO(Role role);

}
