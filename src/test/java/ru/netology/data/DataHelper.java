package ru.netology.data;

import lombok.AllArgsConstructor;
import lombok.Value;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    public static VerificationCode getVerificationCode(AuthInfo authInfo) {
        return new VerificationCode("12345");
    }

    @Value
    @AllArgsConstructor
    public static class UserAccounts {
        String card;
    }

    public static UserAccounts getNumberFirstCard() {

        return new UserAccounts("5559 0000 0000 0001");
    }


    public static UserAccounts getNumberSecondCard() {
        return new UserAccounts("5559 0000 0000 0002");
    }

    public static UserAccounts getEmptyAccounts() {

        return new UserAccounts("");
    }

    public static UserAccounts getNotCorrectCardNumber() {
        return new UserAccounts("5559000000000003");
    }


    public static int getBalanceAfterIncrease(int balance, int amount)
    {
        return balance + amount;
    }

    public static int getBalanceAfterDecrease(int balance, int amount) {

        return balance - amount;
    }
}




