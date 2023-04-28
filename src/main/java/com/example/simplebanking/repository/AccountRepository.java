package com.example.simplebanking.repository;

import com.example.simplebanking.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account,Long>
{
    Account findByAccountNumber(String accountNumber);

    Optional<Account> findByOwner(String owner);

    Optional<Account> findByAccountNumberAndApprovalCode(String accountNumber, String approvalCode);
}
