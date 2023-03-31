package org.internship.jpaonlinebanking.mappers;

import org.internship.jpaonlinebanking.dtos.AccountDTO;
import org.internship.jpaonlinebanking.dtos.AccountTypeDTO;
import org.internship.jpaonlinebanking.entities.Account;
import org.internship.jpaonlinebanking.entities.AccountType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AccountTypeMapper {
    AccountTypeMapper INSTANCE = Mappers.getMapper(AccountTypeMapper.class);

    AccountType fromAccountTypeDTO(AccountTypeDTO dto);
    AccountTypeDTO toAccountTypeDTO(AccountType type);
    List<AccountType> fromListOfAccountTypeDTOs(List<AccountTypeDTO> dtos);
    List<AccountTypeDTO> toListOfAccountTypeDTOs(List<AccountType> types);
}
