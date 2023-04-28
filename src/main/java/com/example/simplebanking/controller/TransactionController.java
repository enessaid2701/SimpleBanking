package com.example.simplebanking.controller;

import com.example.simplebanking.dto.BillPaymentDTO;
import com.example.simplebanking.dto.TransactionDTO;
import com.example.simplebanking.model.Account;
import com.example.simplebanking.model.Transaction;
import com.example.simplebanking.model.TransactionType;
import com.example.simplebanking.repository.AccountRepository;
import com.example.simplebanking.repository.TransactionRepository;
import com.example.simplebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping({"/transaction"})
public class TransactionController
{
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @PutMapping("/addition/{account-number}/{approval-code}")
    public ResponseEntity<String> transactionAddition(@PathVariable("account-number") String accountNumber,
                                                      @PathVariable("approval-code") String approvalCode,
                                                      @RequestBody TransactionDTO transactionDTO)
    {
        Optional<Account> account = accountRepository.findByAccountNumberAndApprovalCode(accountNumber, approvalCode);

        if (account.isPresent()){
            transactionService.depositTransaction(transactionDTO, accountNumber);
        } else {
            String message = "No registered account found matching the entered account number !";
            HttpStatus status = HttpStatus.UNAUTHORIZED;
            return new ResponseEntity<>(message, status);
        }

        String message = "The transaction was performed successfully";
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(message, status);
    }
    @PutMapping("/subtraction/{account-number}/{approval-code}")
    public ResponseEntity<String> transactionSubtraction(@PathVariable("account-number") String accountNumber,
                                                         @PathVariable("approval-code") String approvalCode,
                                                         @RequestBody TransactionDTO transactionDTO)
    {
        Optional<Account> account = accountRepository.findByAccountNumberAndApprovalCode(accountNumber, approvalCode);

        if (account.isPresent()){
            if (account.get().getBalance() >= transactionDTO.getAmount()){
                transactionService.withdrawalTransaction(transactionDTO, accountNumber);
            } else {
                Account currentAccount = accountRepository.findByAccountNumber(accountNumber);
                Transaction transaction = new Transaction();
                transaction.setTransactionType(TransactionType.SUBTRACTION);
                transaction.setAccount(currentAccount);
                transaction.setAmount(transactionDTO.getAmount());
                transaction.setTransactionMessage("Insufficient balance !");
                transactionRepository.save(transaction);

                String message = "Insufficient balance !";
                HttpStatus status = HttpStatus.BAD_REQUEST;
                return new ResponseEntity<>(message, status);
            }
        } else {
            String message = "No registered account found matching the entered account number !";
            HttpStatus status = HttpStatus.UNAUTHORIZED;
            return new ResponseEntity<>(message, status);
        }

        String message = "The transaction was performed successfully";
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(message, status);
    }

    @GetMapping("/get-all-transaction/{account-number}/{approval-code}")
    @ResponseBody
    public Object getAllTransaction(@PathVariable("account-number") String accountNumber,
                                    @PathVariable("approval-code") String approvalCode) {
        Optional<Account> account = accountRepository.findByAccountNumberAndApprovalCode(accountNumber, approvalCode);

        if (account.isPresent()){
            return transactionService.getAllTransactionByAccountNumber(accountNumber, approvalCode);
        }
        else return HttpStatus.UNAUTHORIZED;

    }

    @PostMapping("/create-bill-payment/{account-number}")
    @ResponseBody
    public HttpStatus addBillPayment(@PathVariable("account-number") String accountNumber,
                                     @RequestBody BillPaymentDTO billPaymentDTO){
        return transactionService.addBillPayment(billPaymentDTO, accountNumber);
    }

    @GetMapping("/get-all-bill-payment/{account-number}/{approval-code}")
    @ResponseBody
    public Object getAllBillPayment(@PathVariable("account-number") String accountNumber,
                                    @PathVariable("approval-code") String approvalCode) {
        Optional<Account> account = accountRepository.findByAccountNumberAndApprovalCode(accountNumber, approvalCode);

        if (account.isPresent()){
            return transactionService.getAllBillPayment(accountNumber, approvalCode);
        }
        else return HttpStatus.UNAUTHORIZED;

    }

    @PutMapping("/bill-payment/{account-number}/{approval-code}")
    public ResponseEntity<String> transactionBillPayment(@PathVariable("account-number") String accountNumber,
                                                         @PathVariable("approval-code") String approvalCode,
                                                         @RequestBody BillPaymentDTO billPaymentDTO)
    {
        Optional<Account> account = accountRepository.findByAccountNumberAndApprovalCode(accountNumber, approvalCode);

        if (account.isPresent()){
            if (account.get().getBalance() >= billPaymentDTO.getAmount()){
                transactionService.billPaymentTransaction(billPaymentDTO, accountNumber);
            } else {
                Account currentAccount = accountRepository.findByAccountNumber(accountNumber);
                Transaction transaction = new Transaction();
                transaction.setTransactionType(TransactionType.BILL_PAYMENT);
                transaction.setAccount(currentAccount);
                transaction.setAmount(billPaymentDTO.getAmount());
                transaction.setTransactionMessage(billPaymentDTO.getPayee() + ":" + "Bill Payment not paid!");
                transactionRepository.save(transaction);

                String message = "Insufficient balance !";
                HttpStatus status = HttpStatus.BAD_REQUEST;
                return new ResponseEntity<>(message, status);
            }
        } else {
            String message = "No registered account found matching the entered account number !";
            HttpStatus status = HttpStatus.UNAUTHORIZED;
            return new ResponseEntity<>(message, status);
        }

        String message = "The transaction was performed successfully";
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(message, status);
    }
}
