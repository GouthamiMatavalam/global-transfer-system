package com.assignment.globaltransfersystem.controller;

import com.assignment.globaltransfersystem.model.Account;
import com.assignment.globaltransfersystem.model.Transactions;
import com.assignment.globaltransfersystem.model.TransferRequest;
import com.assignment.globaltransfersystem.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @GetMapping("/accounts/{clientId}")
    public List<Account> getClientAccounts(@PathVariable(name="clientId") Long id) {
        return accountService.getClientAccounts(id);
    }

    @GetMapping("/transactions/{accountId}")
    public List<Transactions> getAccountTransactions(@PathVariable(name="accountId") Long id) {
        return accountService.getAccountTransactions(id);
    }

    @PostMapping("/transfer")
    public Account transferAmount(@RequestBody TransferRequest transferRequest) throws Exception {
        return accountService.transferAmount(transferRequest);
    }
}
