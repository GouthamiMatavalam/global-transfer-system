package com.assignment.globaltransfersystem.util;

import com.assignment.globaltransfersystem.model.Account;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ApplicationUtilsTest {

    Account accountData;
    String bigDecimalData;
    String inputString;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp(){
        accountData = new Account();
        bigDecimalData = "100.213";
        inputString = " ";
    }

    @Test
    public void testStrToBigDecimal(){
        assertNotNull(ApplicationUtils.strToBigDec(bigDecimalData));
    }

    public void testStrToBigDecimalNegative(){
        thrown.expect(java.lang.NumberFormatException.class);
        ApplicationUtils.strToBigDec(inputString);
    }

    @Test
    public void testBalanceAmount()
    {
        accountData.setCurrentBalance(new BigDecimal(1000.10));
        BigDecimal balanceAmount = ApplicationUtils.balanceAmount(accountData, new BigDecimal(bigDecimalData));
        assertNotNull(balanceAmount);
    }

    @Test
    public void testCheckForPositiveBalance()
    {
        accountData.setCurrentBalance(new BigDecimal(1000.10));
        assertTrue(ApplicationUtils.checkForPositiveBalance(accountData, new BigDecimal(bigDecimalData)));
    }

    @Test
    public void testCheckForNegativeBalance()
    {
        accountData.setCurrentBalance(new BigDecimal(10.10));
        assertFalse(ApplicationUtils.checkForPositiveBalance(accountData, new BigDecimal(bigDecimalData)));
    }
}
