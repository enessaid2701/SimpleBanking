package com.example.simplebanking.service;

import com.example.simplebanking.dto.AccountListDTO;
import com.example.simplebanking.dto.CreateAccountRequest;
import com.example.simplebanking.dto.GetAccountResponse;
import com.example.simplebanking.model.Account;
import com.example.simplebanking.repository.AccountRepository;
import com.example.simplebanking.dto.CreateAccountResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class AccountService
{
    @Autowired
    private AccountRepository accountRepository;

    public CreateAccountResponse createAccount(CreateAccountRequest createAccountRequest) {
        Account account = new Account();
        Random random = new Random();
        int code = random.nextInt(9000);

        account.setOwner(createAccountRequest.getOwner());
        account.setAccountNumber(UUID.randomUUID().toString());
        account.setApprovalCode(String.valueOf(code));
        account.setBalance(createAccountRequest.getBalance());
        accountRepository.save(account);

        Optional<Account> accountOptional = accountRepository.findByOwner(account.getOwner());
        CreateAccountResponse createAccountresponse = new CreateAccountResponse();
        createAccountresponse.setOwner(accountOptional.get().getOwner());
        createAccountresponse.setBalance(accountOptional.get().getBalance());
        createAccountresponse.setAccountNumber(accountOptional.get().getAccountNumber());
        createAccountresponse.setApprovalCode(accountOptional.get().getApprovalCode());
        return createAccountresponse;
    }

    public GetAccountResponse getAccount(String accountNumber){
            Account account = accountRepository.findByAccountNumber(accountNumber);
            GetAccountResponse getAccountResponse = new GetAccountResponse();
            if (!Objects.isNull(account)) {
                getAccountResponse.setAccountNumber(account.getAccountNumber());
                getAccountResponse.setOwner(account.getOwner());
                getAccountResponse.setBalance(account.getBalance());
                log.info("Account found! AccountId: {}", account.getId());
            }
            else {
                log.error("Account not found! AccountNumber: {}", accountNumber);
                throw new RuntimeException("Account not found! AccountNumber: " + accountNumber);
            }
            return getAccountResponse;
    }

    public List<AccountListDTO> getAllAccounts()
    {
        Iterator<Account> accountIterator = accountRepository.findAll().iterator();
        List<AccountListDTO> accountListDTOS = new ArrayList<>();
        accountIterator.forEachRemaining(account -> {
            AccountListDTO accountListDTO = new AccountListDTO();
            accountListDTO.setId(account.getId());
            accountListDTO.setOwner(account.getOwner());
            accountListDTO.setAccountNumber(account.getAccountNumber());
            accountListDTO.setBalance(account.getBalance());

            accountListDTOS.add(accountListDTO);
        });

        return accountListDTOS;
    }
}
