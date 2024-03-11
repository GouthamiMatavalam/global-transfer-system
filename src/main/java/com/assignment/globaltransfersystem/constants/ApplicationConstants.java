package com.assignment.globaltransfersystem.constants;

/**
 * ApplicationConstants class will be used to define static fields and exceptions used
 * in application
 *
 * @author Gouthami Matavalam
 *
 */

public class ApplicationConstants {

    // API Response fields
    public static final String RESULT = "result";
    public static final String TRUE = "true";
    public static final String ERROR = "error";

    // TimeZone details
    public static final String TIMEZONE = "Europe/Riga";

    // Transaction Types
    public static final String CREDIT = "CREDIT";
    public static final String TRANSFER = "TRANSFER";

    // Transaction Status
    public static final String FAILED = "FAILED";
    public static final String SUCCESS = "SUCCESS";

    // Transaction Remarks
    public static final String SUCCESS_REMARK = "Transaction Successful";
    public static final String INSUFFICIENT_FUNDS_REASON = "Failed due to Insufficient Funds";

    // Exception messages
    public static final String INSUFFICIENT_FUNDS = "Insufficient Funds in Source account to initiate transaction";
    public static final String CURRENCY_MISMATCH = "Currency mismatch : Please transfer amount with currency ";

    // Generic fields
    public static final String SPACE = " ";

    // Query Params for API Call
    public static final String ACCESS_KEY = "accessKey";
    public static final String FROM_CURRENCY = "fromCurrency";
    public static final String TO_CURRENCY = "toCurrency";
    public static final String AMOUNT = "amount";

}
