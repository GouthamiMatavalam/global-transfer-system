package com.assignment.globaltransfersystem.service;

import com.assignment.globaltransfersystem.constants.ApplicationConstants;
import com.assignment.globaltransfersystem.model.Account;
import com.assignment.globaltransfersystem.model.Transactions;
import com.assignment.globaltransfersystem.model.TransferRequest;
import com.assignment.globaltransfersystem.repository.AccountRepository;
import com.assignment.globaltransfersystem.repository.TransactionRepository;
import com.assignment.globaltransfersystem.util.ApplicationUtils;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AccountService class is used to perfoem business logic
 *
 * @author Gouthami Matavalam
 *
 */

@Service
public class AccountService {

    Logger logger = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    private final RestTemplate restTemplate;

    @Value("${exchange.rate.url}")
    private String url;

    @Value("${exchange.rate.key}")
    private String key;

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
        String[] currencyFields = transferRequest.amount().split(ApplicationConstants.SPACE);

        Account toAccountData = accountRepository.findById(transferRequest.toAccountId()).orElseThrow(() -> new Exception("Provided destination account doesn't exist to transfer amount"));
        Account fromAccountData = accountRepository.findById(transferRequest.fromAccountId()).orElseThrow(() -> new Exception("Provided source account doesn't exist to initiate transaction"));

        // Add condition to consider negative value calculation in amount
        if(toAccountData.getCurrency().equals(currencyFields[1]))
        {
            BigDecimal amountTobeDeductedFromSource = currencyConversion(toAccountData.getCurrency(), fromAccountData.getCurrency(), Long.parseLong(currencyFields[0]));
            if(ApplicationUtils.checkForPositiveBalance(fromAccountData, amountTobeDeductedFromSource)){

                // Add amount to destination account
                toAccountData.setCurrentBalance(toAccountData.getCurrentBalance().add(ApplicationUtils.strToBigDec(currencyFields[0])));
                accountRepository.save(toAccountData);

                // Deduct amount from source account after conversion
                fromAccountData.setCurrentBalance(ApplicationUtils.balanceAmount(fromAccountData, amountTobeDeductedFromSource));
                accountRepository.save(fromAccountData);

                mapAndSaveTransactionRecord(transferRequest.fromAccountId(), transferRequest.toAccountId(), currencyFields, ApplicationConstants.SUCCESS, ApplicationConstants.SUCCESS_REMARK);
            }
            else{
                mapAndSaveTransactionRecord(transferRequest.fromAccountId(), transferRequest.toAccountId(), currencyFields, ApplicationConstants.FAILED, ApplicationConstants.INSUFFICIENT_FUNDS_REASON);
                throw new Exception(ApplicationConstants.INSUFFICIENT_FUNDS);
            }
        }else
        {
            String message = ApplicationConstants.CURRENCY_MISMATCH.concat(toAccountData.getCurrency());
            mapAndSaveTransactionRecord(transferRequest.fromAccountId(), transferRequest.toAccountId(), currencyFields, ApplicationConstants.FAILED, message);
            throw new Exception(message);
        }
        return toAccountData;
    }

    private void mapAndSaveTransactionRecord(Long fromAccountId, Long toAccountId, String[] currencyFields, String status, String remarks)
    {
        Transactions transactions = new Transactions();
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of(ApplicationConstants.TIMEZONE)) ;
        transactions.setAccountId(fromAccountId);
        transactions.setToAccountId(toAccountId);
        transactions.setAmount(ApplicationUtils.strToBigDec(currencyFields[0]));
        transactions.setTransactionType(ApplicationConstants.TRANSFER);
        transactions.setCurrency(currencyFields[1]);
        transactions.setTransactionStatus(status);
        transactions.setCreatedDate(Timestamp.valueOf(zdt.toLocalDateTime()));
        transactions.setRemarks(remarks);

        transactionRepository.save(transactions);
    }

    private BigDecimal currencyConversion(String fromCurrency, String toCurrency, Long amount) throws Exception {

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(ApplicationConstants.ACCESS_KEY, key);
        queryParams.put(ApplicationConstants.FROM_CURRENCY, fromCurrency);
        queryParams.put(ApplicationConstants.TO_CURRENCY, toCurrency);
        queryParams.put(ApplicationConstants.AMOUNT, amount.toString());

        JsonNode apiResponse = getExchangeRate(queryParams);

        if((apiResponse.get(ApplicationConstants.SUCCESS.toLowerCase()).toString()).equalsIgnoreCase(ApplicationConstants.TRUE)) {
            return apiResponse.get(ApplicationConstants.RESULT).decimalValue();
        }
        else {
            throw new Exception(apiResponse.get(ApplicationConstants.ERROR).toPrettyString());
        }
    }

    @CircuitBreaker(name = "getExchangeRate", fallbackMethod = "getExchangeRateFallback")
    private JsonNode getExchangeRate(Map<String, String> queryParams)
    {
        return restTemplate.getForObject(url, JsonNode.class, queryParams);
    }

    private String getExchangeRateFallback(Exception e)
    {
        logger.info("Fallback method response");
        return "SERVICE IS CURRENTLY DOWN, REQUEST TO TRY AFTER SOMETIME !!!";
    }

}
