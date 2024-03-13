package com.assignment.globaltransfersystem.service;

import com.assignment.globaltransfersystem.constants.ApplicationConstants;
import com.assignment.globaltransfersystem.model.Account;
import com.assignment.globaltransfersystem.model.Client;
import com.assignment.globaltransfersystem.model.Transactions;
import com.assignment.globaltransfersystem.model.TransferRequest;
import com.assignment.globaltransfersystem.repository.AccountRepository;
import com.assignment.globaltransfersystem.repository.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AccountService accountService;

    private Client client;
    private List<Account> accountList;
    private List<Transactions> transactionsList;
    private TransferRequest request;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        client = Client.builder()
                .id(Long.valueOf(2))
                .name("Gouthami")
                .build();

        accountList = Arrays.asList(Account.builder()
                .id(45632545L)
                .client(client)
                .currentBalance(new BigDecimal(5463))
                .accountStatus("ACTIVE")
                .currency("EUR")
                .build(), Account.builder()
                .id(45632545L)
                .client(client)
                .currentBalance(new BigDecimal(5463))
                .accountStatus("ACTIVE")
                .currency("INR")
                .build());

        transactionsList = Arrays.asList(Transactions.builder().account(accountList.get(0))
                .accountId(45632545L)
                .amount(new BigDecimal(10))
                .currency("INR").build());

        request = new TransferRequest(25365425L, 524698745L, "100 USD");
    }

    @Test
    public void testGetClientAccounts() {
        when(accountRepository.findByClientId(anyLong())).thenReturn(accountList);
        List<Account> actualAccounts = accountService.getClientAccounts(client.getId());
        assertEquals(accountList, actualAccounts);
    }

    @Test
    public void testGetAccountTransactions(){
        when(transactionRepository.getAccountTransactions(anyLong(), anyInt(), anyInt())).thenReturn(transactionsList);
        List<Transactions> actualTransactions = accountService.getAccountTransactions(accountList.get(0).getId(), 1, 1);
        assertEquals(transactionsList, actualTransactions);
    }

    @Test
    public void testMapAndSaveTransactionRecord() {

        Long fromAccountId = 1L;
        Long toAccountId = 2L;
        String[] currencyFields = {"100.00", "USD"};
        String status = "SUCCESS";
        String remarks = "Test transaction";

        Transactions expectedTransaction = new Transactions();
        expectedTransaction.setAccountId(fromAccountId);
        expectedTransaction.setToAccountId(toAccountId);
        expectedTransaction.setAmount(new BigDecimal("100.00"));
        expectedTransaction.setTransactionType(ApplicationConstants.TRANSFER);
        expectedTransaction.setCurrency("USD");
        expectedTransaction.setTransactionStatus(status);
        expectedTransaction.setRemarks(remarks);

        when(transactionRepository.save(any(Transactions.class))).thenReturn(expectedTransaction);

        accountService.mapAndSaveTransactionRecord(fromAccountId, toAccountId, currencyFields, status, remarks);

        ArgumentCaptor<Transactions> transactionCaptor = ArgumentCaptor.forClass(Transactions.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        Transactions actualTransaction = transactionCaptor.getValue();
        assertEquals(expectedTransaction.getAccountId(), actualTransaction.getAccountId());
    }
}
