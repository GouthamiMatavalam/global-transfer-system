package com.assignment.globaltransfersystem.model;

/**
 * TransferRequest record to map input request for AccountTransferOperation
 *
 * @author Gouthami Matavalam
 *
 */

public record TransferRequest(Long fromAccountId, Long toAccountId, String amount) {
}
