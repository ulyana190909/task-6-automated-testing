package ru.netology.test;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
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
        val loginPage = open("http://localhost:9999/", LoginPage.class);
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    @Order(1)
    public void shouldTransferFromFirstToSecond() {
        val dashboardPage = new DashboardPage();
        val amount = 2000;
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
    @Order(2)
    public void shouldTransferFromSecondToFirst() {
        val dashboardPage = new DashboardPage();
        val amount = 500;
        val currentBalanceFirstCard = dashboardPage.getCurrentBalanceFirstCard();
        val currentBalanceSecondCard = dashboardPage.getCurrentBalanceSecondCard();
        val transmissionPage = dashboardPage.transferToFirstCard();
        transmissionPage.transferBetweenAccounts(getNumberSecondCard(), amount);

        val balanceFirstCardAfterTransmit = dashboardPage.getCurrentBalanceFirstCard();
        val balanceSecondCardAfterTransmit = dashboardPage.getCurrentBalanceSecondCard();
        val expectedBalanceFirstCardAfterTransfer = getBalanceAfterIncrease(currentBalanceFirstCard, amount);
        val expectedBalanceSecondCardAfterTransfer = getBalanceAfterDecrease(currentBalanceSecondCard, amount);
        assertEquals(expectedBalanceFirstCardAfterTransfer, balanceFirstCardAfterTransmit);
        assertEquals(expectedBalanceSecondCardAfterTransfer, balanceSecondCardAfterTransmit);
    }

    @Test
    @Order(3)
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
    @Order(4)
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
    @Order(5)
    void shouldBeErrorIfAmountEmpty() {
        val dashboardPage = new DashboardPage();
        String amount = "";
        val transmissionPage = dashboardPage.transferToSecondCard();
        transmissionPage.transferBetweenAccounts(getNumberFirstCard(), Integer.parseInt(amount));
        transmissionPage.errorTransfer();
    }

    @Test
    @Order(6)
    void shouldBeErrorIfFromInputCardIsEmpty() {
        val dashboardPage = new DashboardPage();
        val amount = 1000;
        val transmissionPage = dashboardPage.transferToSecondCard();
        transmissionPage.transferBetweenAccounts(getNotCorrectCardNumber(), amount);
        transmissionPage.errorTransfer();
    }

    @Test
    @Order(7)
    void shouldBeErrorIfNotCorrectCardNumber() {
        val dashboardPage = new DashboardPage();
        val amount = 1000;
        val transmissionPage = dashboardPage.transferToSecondCard();
        transmissionPage.transferBetweenAccounts(getNotCorrectCardNumber(), amount);
        transmissionPage.errorTransfer();
    }

    @Test
    @Order(8)
    void shouldBeErrorIfAmountMoreThanCurrentBalance() {
        val dashboardPage = new DashboardPage();
        val amount = dashboardPage.getCurrentBalanceSecondCard() * 3;
        val transmissionPage = dashboardPage.transferToFirstCard();
        transmissionPage.transferBetweenAccounts(getNumberSecondCard(), amount);
        transmissionPage.errorTransfer();
    }
}
