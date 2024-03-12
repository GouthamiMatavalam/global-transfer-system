package com.assignment.globaltransfersystem.controller;

import com.assignment.globaltransfersystem.model.Account;
import com.assignment.globaltransfersystem.model.Transactions;
import com.assignment.globaltransfersystem.model.TransferRequest;
import com.assignment.globaltransfersystem.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AccountController class will be used for api requests mapping and
 * send response
 *
 * @author Gouthami Matavalam
 *
 */

@RestController
@RequestMapping("/api/v1")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @RequestMapping(value = { "/accounts/{clientId}" }, method = { RequestMethod.GET }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<Account>> getClientAccounts(@PathVariable(name="clientId") Long id) {
        return new ResponseEntity<> (accountService.getClientAccounts(id), HttpStatus.OK);
    }

    @RequestMapping(value = { "/transactions/{accountId}" }, method = { RequestMethod.GET }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<Transactions>> getAccountTransactions(@PathVariable(name="accountId") Long id) {
        return new ResponseEntity<>(accountService.getAccountTransactions(id), HttpStatus.OK);
    }

    @RequestMapping(value = { "/transfer" }, method = { RequestMethod.POST }, produces = {
            MediaType.APPLICATION_JSON_VALUE }, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Account> transferAmount(@RequestBody TransferRequest transferRequest) throws Exception {
        return new ResponseEntity<>(accountService.transferAmount(transferRequest), HttpStatus.OK);
    }
}
