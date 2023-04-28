package com.example.simplebanking.repository;

import com.example.simplebanking.model.Account;
import com.example.simplebanking.model.BillPayment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillPaymentRepository extends CrudRepository<BillPayment, Long>
{
    List<BillPayment> findAllByAccount(Optional<Account> account);

    BillPayment findAllByAccountAndPaye(Account account, String paye);

}
