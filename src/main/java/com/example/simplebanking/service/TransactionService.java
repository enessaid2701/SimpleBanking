package com.example.simplebanking.service;

import com.example.simplebanking.dto.BillPaymentDTO;
import com.example.simplebanking.dto.TransactionListDTO;
import com.example.simplebanking.dto.TransactionDTO;
import com.example.simplebanking.model.*;
import com.example.simplebanking.repository.AccountRepository;
import com.example.simplebanking.repository.BillPaymentRepository;
import com.example.simplebanking.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class TransactionService
{
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BillPaymentRepository billPaymentRepository;

    public HttpStatus depositTransaction(TransactionDTO transactionDTO, String accountNumber)
    {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        double balance = account.getBalance();
        double newBalance = balance + transactionDTO.getAmount();
        account.setBalance(newBalance);
        accountRepository.save(account);

        Account currentAccount = accountRepository.findByAccountNumber(accountNumber);
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setTransactionType(TransactionType.ADDITION);
        transaction.setAccount(currentAccount);
        transaction.setTransactionMessage("Transaction Successful");
        transactionRepository.save(transaction);

        return HttpStatus.OK;
    }

    public HttpStatus withdrawalTransaction(TransactionDTO transactionDTO, String accountNumber)
    {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        double balance = account.getBalance();
        double newBalance = balance - transactionDTO.getAmount();
        account.setBalance(newBalance);
        accountRepository.save(account);

        Account currentAccount = accountRepository.findByAccountNumber(accountNumber);
        String transactionMessage = "Transaction Successful";
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setTransactionType(TransactionType.SUBTRACTION);
        transaction.setAccount(currentAccount);
        transaction.setTransactionMessage(transactionMessage);
        transactionRepository.save(transaction);

        return HttpStatus.OK;
    }

    public HttpStatus addBillPayment(BillPaymentDTO billPaymentDTO, String accountNumber){
        Account account = accountRepository.findByAccountNumber(accountNumber);
        BillPayment billPayment = new BillPayment();
        billPayment.setAccount(account);
        billPayment.setPaye(billPaymentDTO.getPayee());
        billPayment.setAmount(billPaymentDTO.getAmount());
        billPayment.setBillPaymentStatus(BillPaymentStatus.NOT_PAID);
        billPaymentRepository.save(billPayment);
        return HttpStatus.OK;
    }

    public HttpStatus billPaymentTransaction(BillPaymentDTO billPaymentDTO, String accountNumber)
    {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        BillPayment billPayment = billPaymentRepository.findAllByAccountAndPaye(account, billPaymentDTO.getPayee());
        double balance = account.getBalance();
        double newBalance = balance - billPaymentDTO.getAmount();
        account.setBalance(newBalance);
        accountRepository.save(account);
        billPayment.setBillPaymentStatus(BillPaymentStatus.PAID);
        billPaymentRepository.save(billPayment);

        Account currentAccount = accountRepository.findByAccountNumber(accountNumber);
        String transactionMessage = "Transaction Successful";
        Transaction transaction = new Transaction();
        transaction.setAmount(billPaymentDTO.getAmount());
        transaction.setTransactionType(TransactionType.BILL_PAYMENT);
        transaction.setAccount(currentAccount);
        transaction.setTransactionMessage(transactionMessage);
        transactionRepository.save(transaction);

        return HttpStatus.OK;
    }

    public List<TransactionListDTO> getAllTransactionByAccountNumber(String accountNumber, String approvalCode)
    {

        Optional<Account> account = accountRepository.findByAccountNumberAndApprovalCode(accountNumber, approvalCode);

        Iterator<Transaction> transactionIterator = transactionRepository.findAllByAccount(account).stream().iterator();
        List<TransactionListDTO> transactionListDTOS = new ArrayList<>();
        transactionIterator.forEachRemaining(transaction -> {
            TransactionListDTO transactionListDTO = new TransactionListDTO();
            transactionListDTO.setDate(transaction.getCreatedAt());
            transactionListDTO.setTransactionMessage(transaction.getTransactionMessage());
            transactionListDTO.setTransactionType(transaction.getTransactionType().toString());

            transactionListDTO.setAmount(transaction.getAmount());

            transactionListDTOS.add(transactionListDTO);
        });

        return transactionListDTOS;
    }

    public List<BillPaymentDTO> getAllBillPayment(String accountNumber, String approvalCode)
    {

        Optional<Account> account = accountRepository.findByAccountNumberAndApprovalCode(accountNumber, approvalCode);

        Iterator<BillPayment> billPaymentIterator = billPaymentRepository.findAllByAccount(account).stream().iterator();
        List<BillPaymentDTO> billPaymentDTOS = new ArrayList<>();
        billPaymentIterator.forEachRemaining(billPayment -> {
            BillPaymentDTO billPaymentDTO = new BillPaymentDTO();
            billPaymentDTO.setAmount(billPayment.getAmount());
            billPaymentDTO.setPayee(billPayment.getPaye());
            billPaymentDTO.setBillPaymentStatus(billPayment.getBillPaymentStatus());

            billPaymentDTOS.add(billPaymentDTO);
        });

        return billPaymentDTOS;
    }

}
