package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;


import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;

public class MoneyTransferTest {

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999/");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        verificationPage.validVerify(verificationCode);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        dashboardPage.assertDashboardPage();
    }

    @Test
    public void shouldTransferFromFirstToSecond() {
        val dashboardPage = new DashboardPage();
        val amount = 500;
        val currentBalanceFirstCard = dashboardPage.getCurrentBalanceFirstCard();
        val currentBalanceSecondCard = dashboardPage.getCurrentBalanceSecondCard();
        val transferPage = dashboardPage.transferToSecondCard();
        transferPage.transferBetweenAccounts(getNumberFirstCard(), amount);

        val balanceFirstCardAfterTransmit = dashboardPage.getCurrentBalanceFirstCard();
        val balanceSecondCardAfterTransmit = dashboardPage.getCurrentBalanceSecondCard();
        val expectedBalanceFirstCardAfterTransfer = getBalanceAfterDecrease(currentBalanceFirstCard, amount);
        val expectedBalanceSecondCardAfterTransfer = getBalanceAfterIncrease(currentBalanceSecondCard, amount);
        assertEquals(expectedBalanceFirstCardAfterTransfer, balanceFirstCardAfterTransmit);
        assertEquals(expectedBalanceSecondCardAfterTransfer, balanceSecondCardAfterTransmit);
    }

    @Test
    public void shouldTransferFromSecondToFirst() {
        val dashboardPage = new DashboardPage();
        val amount = 1500;
        val currentBalanceFirstCard = dashboardPage.getCurrentBalanceFirstCard();
        val currentBalanceSecondCard = dashboardPage.getCurrentBalanceSecondCard();
        val transmissionPage = dashboardPage.transferToFirstCard();
        transmissionPage.transferBetweenAccounts(getNumberSecondCard(), amount);

        val balanceFirstCardAfterTransmit = dashboardPage.getCurrentBalanceFirstCard();
        val balanceSecondCardAfterTransmit = dashboardPage.getCurrentBalanceSecondCard();
        val expectedBalanceFirstCardAfterTransfer = getBalanceAfterDecrease(currentBalanceFirstCard, amount);
        val expectedBalanceSecondCardAfterTransfer = getBalanceAfterIncrease(currentBalanceSecondCard, amount);
        assertEquals(expectedBalanceFirstCardAfterTransfer, balanceFirstCardAfterTransmit);
        assertEquals(expectedBalanceSecondCardAfterTransfer, balanceSecondCardAfterTransmit);
    }

    @Test
    public void shouldTranferZero() {
        val dashboardPage = new DashboardPage();
        val amount = 0;
        val currentBalanceFirstCard = dashboardPage.getCurrentBalanceFirstCard();
        val currentBalanceSecondCard = dashboardPage.getCurrentBalanceSecondCard();
        val transmissionPage = dashboardPage.transferToSecondCard();
        transmissionPage.transferBetweenAccounts(getNumberFirstCard(), amount);

        val balanceFirstCardAfterTransmit = dashboardPage.getCurrentBalanceFirstCard();
        val balanceSecondCardAfterTransmit = dashboardPage.getCurrentBalanceSecondCard();
        val expectedBalanceFirstCardAfterTransfer = getBalanceAfterDecrease(currentBalanceFirstCard, amount);
        val expectedBalanceSecondCardAfterTransfer = getBalanceAfterIncrease(currentBalanceSecondCard, amount);
        assertEquals(expectedBalanceFirstCardAfterTransfer, balanceFirstCardAfterTransmit);
        assertEquals(expectedBalanceSecondCardAfterTransfer, balanceSecondCardAfterTransmit);
    }

    @Test
    void shouldBeTransferAmountEqualsCurrentBalance() {
        val dashboardPage = new DashboardPage();
        val amount = dashboardPage.getCurrentBalanceFirstCard();
        val currentBalanceFirstCard = dashboardPage.getCurrentBalanceFirstCard();
        val currentBalanceSecondCard = dashboardPage.getCurrentBalanceSecondCard();
        val transmissionPage = dashboardPage.transferToSecondCard();
        transmissionPage.transferBetweenAccounts(getNumberFirstCard(), amount);

        val balanceFirstCardAfterTransmit = dashboardPage.getCurrentBalanceFirstCard();
        val balanceSecondCardAfterTransmit = dashboardPage.getCurrentBalanceSecondCard();
        val expectedBalanceFirstCardAfterTransfer = getBalanceAfterDecrease(currentBalanceFirstCard, amount);
        val expectedBalanceSecondCardAfterTransfer = getBalanceAfterIncrease(currentBalanceSecondCard, amount);
        assertEquals(expectedBalanceFirstCardAfterTransfer, balanceFirstCardAfterTransmit);
        assertEquals(expectedBalanceSecondCardAfterTransfer, balanceSecondCardAfterTransmit);
    }

    @Test
    void shouldBeErrorIfAmountEmpty() {
        val dashboardPage = new DashboardPage();
        String amount = "";
        val transmissionPage = dashboardPage.transferToSecondCard();
        transmissionPage.transferBetweenAccounts(getNumberFirstCard(), Integer.parseInt(amount));
        transmissionPage.errorTransfer();
    }

    @Test
    void shouldBeErrorIfFromInputCardIsEmpty() {
        val dashboardPage = new DashboardPage();
        val amount = 4000;
        val transmissionPage = dashboardPage.transferToSecondCard();
        transmissionPage.transferBetweenAccounts(getNotCorrectCardNumber(), amount);
        transmissionPage.errorTransfer();
    }

    @Test
    void shouldBeErrorIfNotCorrectCardNumber() {
        val dashboardPage = new DashboardPage();
        val amount = 3000;
        val transmissionPage = dashboardPage.transferToSecondCard()
        transmissionPage.transferBetweenAccounts(getNotCorrectCardNumber(), amount);
        transmissionPage.errorTransfer();
    }

    @Test
    void shouldBeErrorIfAmountMoreThanCurrentBalance() {
        val dashboardPage = new DashboardPage();
        val amount = dashboardPage.getCurrentBalanceSecondCard() * 2;
        val transmissionPage = dashboardPage.transferToFirstCard()
        transmissionPage.transferBetweenAccounts(getNumberSecondCard(), amount);
        transmissionPage.errorTransfer();
    }
}
}