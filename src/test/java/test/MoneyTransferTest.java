package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;

public class MoneyTransferTest {
    DashboardPage dashboardPage;

    @BeforeEach
    void setup() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = getVerificationCode();
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    void shuldTransferMoneyBetweenCards() {
        var cardInfoFirstCard = DataHelper.getfirstCardInfo();
        var cardInfoSecondCard = DataHelper.getsecondCardInfo();
        var balanceFirstCard = dashboardPage.getCardBalance(cardInfoFirstCard);
        var balanceSecondCard = dashboardPage.getCardBalance(cardInfoSecondCard);
        var amount = generateValidAmount(balanceFirstCard);
        var expecredBalanceFirstCard = balanceFirstCard - amount;
        var expectedBalanceSecondCard = balanceSecondCard + amount;
        var transferPage = dashboardPage.selectCardToTransfer(cardInfoSecondCard);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), cardInfoFirstCard);
        var actualBalanceFirstCard = dashboardPage.getCardBalance(cardInfoFirstCard);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(cardInfoSecondCard);
        assertEquals(expecredBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }
    @Test
    void transferInvalidAmountFromFirstCardToSecondCard() {

        var cardInfoFirstCard = DataHelper.getfirstCardInfo();
        var cardInfoSecondCard = DataHelper.getsecondCardInfo();
        var balanceFirstCard = dashboardPage.getCardBalance(cardInfoFirstCard);
        var balanceSecondCard = dashboardPage.getCardBalance(cardInfoSecondCard);
        var amount = generationInvalidAmount(balanceSecondCard);
        var transferPage = dashboardPage.selectCardToTransfer(cardInfoSecondCard);
        transferPage.makeTransfer(String.valueOf(amount),cardInfoSecondCard);
        transferPage.findError("Сумма перевода превышает баланс карты для списания");
        var actualBalanceSecondCard = dashboardPage.getCardBalance(cardInfoSecondCard);
        var actualBalanceFirstCard = dashboardPage.getCardBalance(cardInfoFirstCard);
        assertEquals(balanceFirstCard,actualBalanceFirstCard);
        assertEquals(balanceSecondCard,actualBalanceSecondCard);
    }
    @Test
    void transferInvalidAmountMinusFromFirstCardToSecondCard() {
            var cardInfoFirstCard = DataHelper.getfirstCardInfo();
            var cardInfoSecondCard = DataHelper.getsecondCardInfo();
            var balanceFirstCard = dashboardPage.getCardBalance(cardInfoFirstCard);
            var balanceSecondCard = dashboardPage.getCardBalance(cardInfoSecondCard);
            var amount = generationInvalidAmount(balanceFirstCard);
            var expecredBalanceFirstCard = balanceFirstCard - amount;
            var expectedBalanceSecondCard = balanceSecondCard + amount;
            var transferPage = dashboardPage.selectCardToTransfer(cardInfoSecondCard);
            dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), cardInfoFirstCard);
            var actualBalanceFirstCard = dashboardPage.getCardBalance(cardInfoFirstCard);
            var actualBalanceSecondCard = dashboardPage.getCardBalance(cardInfoSecondCard);
            assertEquals(expecredBalanceFirstCard, actualBalanceFirstCard);
            assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
        }
    }


