package com.example.simplebanking.repository;

import com.example.simplebanking.model.Account;
import com.example.simplebanking.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long>
{
    List<Transaction> findAllByAccount(Optional<Account> account);
}
