package com.example.simplebanking.controller;

import com.example.simplebanking.dto.AccountListDTO;
import com.example.simplebanking.dto.CreateAccountRequest;
import com.example.simplebanking.dto.CreateAccountResponse;
import com.example.simplebanking.dto.GetAccountResponse;
import com.example.simplebanking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping({ "/account"})
public class AccountController
{
    @Autowired
    private AccountService accountService;

    @PostMapping("/create-account")
    @ResponseBody
    public CreateAccountResponse createAccount(@RequestBody CreateAccountRequest createAccountRequest){
        CreateAccountResponse createAccountResponse = accountService.createAccount(createAccountRequest);
        return createAccountResponse;
    }

    @GetMapping("/get-account/{account-number}")
    @ResponseBody
    public GetAccountResponse getAccount(@PathVariable("account-number") String accountNumber){
        return accountService.getAccount(accountNumber);
    }

    @GetMapping("/get-all-account")
    @ResponseBody
    public List<AccountListDTO> getAllAccount() {
        return accountService.getAllAccounts();
    }
}
