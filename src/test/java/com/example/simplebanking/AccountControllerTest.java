package com.example.simplebanking.controller;

import com.example.simplebanking.dto.AccountListDTO;
import com.example.simplebanking.dto.CreateAccountRequest;
import com.example.simplebanking.dto.CreateAccountResponse;
import com.example.simplebanking.dto.GetAccountResponse;
import com.example.simplebanking.service.AccountService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    public void testCreateAccount() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest("Account", 200.0);
        CreateAccountResponse response = new CreateAccountResponse("Account5","9876543210","1243",100.0);

        when(accountService.createAccount(any(CreateAccountRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/account/create-account")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"owner\":\"Account\",\"accountNumber\":\"1234567890\",\"approvalCode\":\"1234\"}"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(content().json("{\"accountNumber\":\"9876543210\"}"));
    }

    @Test
    public void testGetAccount() throws Exception {
        GetAccountResponse response = new GetAccountResponse("Account", "9876543210", 1000.00);

        when(accountService.getAccount(eq("9876543210"))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/account/get-account/9876543210"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(content().json("{\"owner\":\"Account\",\"accountNumber\":\"9876543210\",\"balance\":\"1000.00\"}"));
    }

    @Test
    public void testGetAllAccount() throws Exception {
        List<AccountListDTO> accounts = Arrays.asList(
            new AccountListDTO(1,"Account4", "9876543210",100.0),
            new AccountListDTO(2,"Account5", "9876543214",200.0)
        );

        when(accountService.getAllAccounts()).thenReturn(accounts);

        mockMvc.perform(MockMvcRequestBuilders.get("/account/get-all-account"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(content().json("[{\"owner\":\"Account\",\"accountNumber\":\"9876543210\"},{\"owner\":\"Account1\",\"accountNumber\":\"1234567890\"}]"));
    }
}
