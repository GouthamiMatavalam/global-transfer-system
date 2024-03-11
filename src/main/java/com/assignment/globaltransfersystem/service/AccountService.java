package com.assignment.globaltransfersystem.service;

import com.assignment.globaltransfersystem.model.Account;
import com.assignment.globaltransfersystem.model.Transactions;
import com.assignment.globaltransfersystem.model.TransferRequest;
import com.assignment.globaltransfersystem.repository.AccountRepository;
import com.assignment.globaltransfersystem.repository.TransactionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    private final RestTemplate restTemplate;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository, RestTemplate restTemplate)
    {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.restTemplate = restTemplate;
    }

    public List<Account> getClientAccounts(Long id){
        return accountRepository.findByClientId(id);
    }

    public List<Transactions> getAccountTransactions(Long id){
        return transactionRepository.findByAccountIdOrderByCreatedDateDesc(id);
    }

    @Transactional
    public Account transferAmount(TransferRequest transferRequest) throws Exception {
        String[] currencyFields = transferRequest.amount().split(" ");

        Account toAccountData = accountRepository.findById(transferRequest.toAccountId()).orElseThrow(() -> new Exception("Provided destination account doesn't exist to transfer amount"));
        Account fromAccountData = accountRepository.findById(transferRequest.fromAccountId()).orElseThrow(() -> new Exception("Provided source account doesn't exist to initiate transaction"));

        // Add condition to consider negative value calculation in amount
        if(toAccountData.getCurrency().equals(currencyFields[1]))
        {
            Long amountTobeDeductedFromSource = currencyConversion(toAccountData.getCurrency(), fromAccountData.getCurrency(), Long.parseLong(currencyFields[0]));
            if(fromAccountData.getCurrentBalance() - amountTobeDeductedFromSource >0 ){

                // Add amount to destination account
                toAccountData.setCurrentBalance(toAccountData.getCurrentBalance() + Long.parseLong(currencyFields[0]));
                accountRepository.save(toAccountData);

                // Deduct amount from source account after conversion
                fromAccountData.setCurrentBalance(fromAccountData.getCurrentBalance() - amountTobeDeductedFromSource);
                accountRepository.save(fromAccountData);

                mapAndSaveTransactionRecord(transferRequest.fromAccountId(), transferRequest.toAccountId(), currencyFields, "SUCCESS", "Transaction Successful");
            }
            else{
                mapAndSaveTransactionRecord(transferRequest.fromAccountId(), transferRequest.toAccountId(), currencyFields, "FAILED", "Failed due to Insufficient Funds");

                throw new Exception("Insufficient Funds in Source account to initiate transaction");
            }
        }else
        {
            String message = "\"Currency mismatch : Account has currency \" + toAccountData.getCurrency() +\" and transfer amount is in \" + currencyFields[1]\n" +
                    "+ \" request to match currency with account\"";
            mapAndSaveTransactionRecord(transferRequest.fromAccountId(), transferRequest.toAccountId(), currencyFields, "FAILED", message);
            throw new Exception(message);
        }
        return toAccountData;
    }

    private void mapAndSaveTransactionRecord(Long fromAccountId, Long toAccountId, String[] currencyFields, String status, String remarks)
    {
        Transactions transactions = new Transactions();
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Europe/Riga")) ;
        transactions.setAccountId(fromAccountId);
        transactions.setToAccountId(toAccountId);
        transactions.setAmount(Long.parseLong(currencyFields[0]));
        transactions.setTransactionType("TRANSFER");
        transactions.setCurrency(currencyFields[1]);
        transactions.setTransactionStatus(status);
        transactions.setCreatedDate(Timestamp.valueOf(zdt.toLocalDateTime()));
        transactions.setRemarks(remarks);

        transactionRepository.save(transactions);
    }

    private Long currencyConversion(String fromCurrency, String toCurrency, Long amount) throws Exception {
        JsonNode apiResponse = restTemplate.getForObject("http://api.exchangerate.host/convert?" +
                "access_key=71a1f5a580a6b44215f66d44d8548cb4&from="+fromCurrency+"&to="+
                toCurrency+"&amount=" +100, JsonNode.class);

        if((apiResponse.get("success").toString()).equals("true")) {
            return apiResponse.get("result").longValue();
        }
        else {
            throw new Exception(apiResponse.get("error").toPrettyString());
        }
    }

}
