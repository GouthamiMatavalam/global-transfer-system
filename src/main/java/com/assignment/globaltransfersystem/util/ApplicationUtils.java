package com.assignment.globaltransfersystem.util;

import com.assignment.globaltransfersystem.model.Account;

import java.math.BigDecimal;

/**
 * ApplicationUtils class is used to keep repeatedly required actions in common place
 *
 * @author Gouthami Matavalam
 *
 */

public class ApplicationUtils {

    // Convert from String to Bigdecimal
    public static BigDecimal strToBigDec(String str)
    {
        return BigDecimal.valueOf(Double.valueOf(str));
    }

    // Deducts amount from source account
    public static BigDecimal balanceAmount(Account fromAccountData, BigDecimal amountTobeDeductedFromSource)
    {
        return fromAccountData.getCurrentBalance().subtract(amountTobeDeductedFromSource);
    }

    // validates if the transfer amount is greater than account balance
    public static boolean checkForPositiveBalance(Account fromAccountData, BigDecimal amountTobeDeductedFromSource){
        if(balanceAmount(fromAccountData, amountTobeDeductedFromSource).compareTo(BigDecimal.ZERO) > 0)
            return true;
        else
            return false;
    }
}
