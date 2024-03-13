package com.assignment.globaltransfersystem.controller;

import com.assignment.globaltransfersystem.model.Account;
import com.assignment.globaltransfersystem.model.Client;
import com.assignment.globaltransfersystem.model.Transactions;
import com.assignment.globaltransfersystem.model.TransferRequest;
import com.assignment.globaltransfersystem.service.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.SerializationFeature;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    private static AccountService accountService = mock(AccountService.class);
    private static MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new AccountController(accountService)).build();

    private List<Account> accountList;
    private List<Transactions> transactionsList;
    private TransferRequest request;

    @Before
    public void setUp() {
        Client client = Client.builder()
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

        request = new TransferRequest(25365425L, 524698745L, "100 INR");
    }

    @Test
    public void getClientAccountsTest() throws Exception{
        when(accountService.getClientAccounts(anyLong())).thenReturn(accountList);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getClientAccountsPostTest() throws Exception{
        when(accountService.getClientAccounts(anyLong())).thenReturn(accountList);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts/2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
    }

    @Test
    public void getAccountTransactionsTest() throws Exception{
        when(accountService.getAccountTransactions(anyLong(), anyInt(), anyInt())).thenReturn(transactionsList);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/transactions/2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void transferAmountTest() throws Exception{
        when(accountService.transferAmount(any(TransferRequest.class))).thenReturn(accountList.get(0));
        ObjectMapper mapper = new ObjectMapper();
        // Disabled to avoid exception while using Record class
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/transfer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}