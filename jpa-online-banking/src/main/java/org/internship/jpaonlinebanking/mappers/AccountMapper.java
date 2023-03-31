package org.internship.jpaonlinebanking.mappers;

import org.internship.jpaonlinebanking.dtos.AccountDTO;
import org.internship.jpaonlinebanking.dtos.AccountTypeDTO;
import org.internship.jpaonlinebanking.dtos.UserDTO;
import org.internship.jpaonlinebanking.entities.Account;
import org.internship.jpaonlinebanking.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {AccountTypeMapper.class})
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);
    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "user", ignore = true)
    Account fromAccountDTO(AccountDTO dto);
    AccountDTO toAccountDTO(Account account);
    List<Account> fromListOfAccountDTOs(List<AccountDTO> dtos);
    List<AccountDTO> toListOfAccountDTOs(List<Account> accounts);
}
