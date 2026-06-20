package com.saucedemo.stepdefinitions;

import com.saucedemo.pages.CheckoutConfirmationPage;
import com.saucedemo.pages.CheckoutDetailsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class CheckoutSteps {

    private CheckoutDetailsPage checkoutDetailsPage;
    private CheckoutConfirmationPage checkoutConfirmationPage;

    private CheckoutDetailsPage detailsPage() {
        if (checkoutDetailsPage == null) {
            checkoutDetailsPage = new CheckoutDetailsPage();
        }
        return checkoutDetailsPage;
    }

    private CheckoutConfirmationPage overviewPage() {
        if (checkoutConfirmationPage == null) {
            checkoutConfirmationPage = new CheckoutConfirmationPage();
        }
        return checkoutConfirmationPage;
    }

    // --- Checkout details (Step One) ---

    @Then("the checkout page should be displayed")
    public void theCheckoutPageShouldBeDisplayed() {
        assertThat(detailsPage().isCheckoutPageDisplayed())
            .as("Expected the checkout details page to be displayed")
            .isTrue();
    }

    @When("the user enters checkout details {string} {string} {string}")
    public void theUserEntersCheckoutDetails(String firstName, String lastName, String postalCode) {
        detailsPage().fillCheckoutDetails(firstName, lastName, postalCode);
    }

    @And("the user continues to the order overview")
    public void theUserContinuesToTheOrderOverview() {
        detailsPage().clickContinue();
    }

    @Then("a checkout error message should be displayed")
    public void aCheckoutErrorMessageShouldBeDisplayed() {
        assertThat(detailsPage().isErrorDisplayed())
            .as("Expected a validation error message on the checkout details page")
            .isTrue();
    }

    @When("the user cancels the checkout from the details page")
    public void theUserCancelsTheCheckoutFromTheDetailsPage() {
        detailsPage().clickCancel();
    }

    // --- Order overview (Step Two) ---

    @Then("the order overview page should be displayed")
    public void theOrderOverviewPageShouldBeDisplayed() {
        assertThat(overviewPage().isCheckoutFinalPageDisplayed())
            .as("Expected the order overview page to be displayed")
            .isTrue();
    }

    @And("the order summary should contain {string}")
    public void theOrderSummaryShouldContain(String productName) {
        assertThat(overviewPage().isItemPresentInOrder(productName))
            .as("Expected '" + productName + "' to appear in the order summary")
            .isTrue();
    }

    @When("the user finishes the checkout")
    public void theUserFinishesTheCheckout() {
        overviewPage().clickFinish();
    }

    @When("the user cancels the checkout from the overview page")
    public void theUserCancelsTheCheckoutFromTheOverviewPage() {
        overviewPage().clickCancel();
    }

    @Then("the order total should equal the subtotal plus tax")
    public void theOrderTotalShouldEqualTheSubtotalPlusTax() {
        double expectedTotal = overviewPage().getSubtotalValue() + overviewPage().getTaxValue();
        assertThat(overviewPage().getTotalValue())
            .as("Expected total to equal subtotal + tax")
            .isCloseTo(expectedTotal, within(0.01));
    }

    // --- Order confirmation (Step Three) ---

    @Then("the order should be confirmed")
    public void theOrderShouldBeConfirmed() {
        assertThat(overviewPage().isOrderConfirmed())
            .as("Expected the order confirmation page to be displayed after finishing checkout")
            .isTrue();
    }
}

