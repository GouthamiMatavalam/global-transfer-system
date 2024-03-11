package com.assignment.globaltransfersystem.model;

public record TransferRequest(Long fromAccountId, Long toAccountId, String amount) {
}
