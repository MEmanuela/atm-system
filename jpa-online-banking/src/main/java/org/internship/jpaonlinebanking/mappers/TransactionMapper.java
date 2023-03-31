package org.internship.jpaonlinebanking.mappers;

import org.internship.jpaonlinebanking.dtos.AccountDTO;
import org.internship.jpaonlinebanking.dtos.TransactionDTO;
import org.internship.jpaonlinebanking.entities.Account;
import org.internship.jpaonlinebanking.entities.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {TransactionTypeMapper.class})
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "baseAccount", ignore = true)
    @Mapping(target = "receivingAccount", ignore = true)
    Transaction fromTransactionDTO(TransactionDTO dto);
    TransactionDTO toTransactionDTO(Transaction transaction);
    List<Transaction> fromListOfTransactionDTOs(List<TransactionDTO> dtos);
    List<TransactionDTO> toListOfTransactionDTOs(List<Transaction> transactions);
    List<List<Transaction>> fromListOfListsOfTransactionDTOs(List<List<TransactionDTO>> dtosList);
    List<List<TransactionDTO>> toListOfListsOfTransactionDTOs(List<List<Transaction>> transactionsList);
}
