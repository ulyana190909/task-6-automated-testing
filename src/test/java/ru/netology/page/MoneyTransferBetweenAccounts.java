package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;


public class MoneyTransferBetweenAccounts {
    private final SelenideElement amountInput = $("[data-test-id ='amount'] input");
    private final SelenideElement fromWhere = $("[data-test-id = from] input");
    private final SelenideElement replenishButton = $("[data-test-id ='action-transfer']");
    private final SelenideElement error = $("[data-test-id ='error-notification']");

    public MoneyTransferBetweenAccounts() {
        SelenideElement heading = $(byText("Пополнение карты"));
        heading.shouldBe(Condition.visible);
    }

    public void transferBetweenAccounts(DataHelper.UserAccounts UserAccounts, int amount) {
        amountInput.setValue(String.valueOf(amount));
        fromWhere.setValue(UserAccounts.getCard());
        replenishButton.click();
        new DashboardPage();
    }

    public void errorTransfer() {
        error.shouldBe(Condition.visible);
    }
}
