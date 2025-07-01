package org.example.cnjava_milkteastore.backend.account.repository;

import org.springframework.stereotype.Repository;
import org.example.cnjava_milkteastore.backend.account.entity.Account;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {
}