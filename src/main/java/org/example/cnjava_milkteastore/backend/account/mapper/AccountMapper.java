package org.example.cnjava_milkteastore.backend.account.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.example.cnjava_milkteastore.backend.account.dto.AccountDTO;
import org.example.cnjava_milkteastore.backend.account.entity.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDTO toDTO(Account entity);
    Account toEntity(AccountDTO dto);
}