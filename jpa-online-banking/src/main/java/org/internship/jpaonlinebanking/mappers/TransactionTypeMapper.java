package org.internship.jpaonlinebanking.mappers;

import org.internship.jpaonlinebanking.dtos.AccountTypeDTO;
import org.internship.jpaonlinebanking.dtos.TransactionTypeDTO;
import org.internship.jpaonlinebanking.entities.AccountType;
import org.internship.jpaonlinebanking.entities.TransactionType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TransactionTypeMapper {
    TransactionTypeMapper INSTANCE = Mappers.getMapper(TransactionTypeMapper.class);
    TransactionType fromTransactionTypeDTO(TransactionTypeDTO dto);
    TransactionTypeDTO toTransactionTypeDTO(TransactionType type);
    List<TransactionType> fromListOfTransactionTypeDTOs(List<TransactionTypeDTO> dtos);
    List<TransactionTypeDTO> toListOfTransactionTypeDTOs(List<TransactionType> types);
}
