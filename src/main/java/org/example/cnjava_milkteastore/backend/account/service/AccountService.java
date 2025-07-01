package org.example.cnjava_milkteastore.backend.account.service;

import org.example.cnjava_milkteastore.backend.account.dto.AccountDTO;
import org.example.cnjava_milkteastore.backend.account.entity.Account;
import org.example.cnjava_milkteastore.backend.account.repository.AccountRepository;
import org.example.cnjava_milkteastore.backend.account.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountMapper accountMapper;

    @Transactional
    public AccountDTO createOrUpdate(AccountDTO dto) {
        Account entity;
        if (dto.getID() != 0) {
            entity = accountRepository.findById(dto.getID()).orElse(new Account());
            entity.setID(dto.getID());
            entity.setUserName(dto.getUserName());
            entity.setPassword(dto.getPassword());
            entity.setRole(dto.getRole());
            entity.setEmployeeID(dto.getEmployeeID());
        } else {
            entity = accountMapper.toEntity(dto);
        }
        Account saved = accountRepository.save(entity);
        return accountMapper.toDTO(saved);
    }

    public void delete(AccountDTO dto) {
        Account entity = accountMapper.toEntity(dto);
        accountRepository.delete(entity);
    }

    public void deleteById(int id) {
        accountRepository.deleteById(id);
    }

    public void deleteAll() {
        accountRepository.deleteAll();
    }

    public List<AccountDTO> getAll() {
        return ((List<Account>) accountRepository.findAll())
                .stream().map(accountMapper::toDTO).collect(Collectors.toList());
    }

    public Optional<AccountDTO> getById(int id) {
        return accountRepository.findById(id).map(accountMapper::toDTO);
    }

}