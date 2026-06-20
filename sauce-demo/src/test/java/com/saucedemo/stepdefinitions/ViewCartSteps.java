package com.saucedemo.stepdefinitions;

import com.saucedemo.pages.AddToCartPage;
import com.saucedemo.pages.ViewCartPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class ViewCartSteps {

    private AddToCartPage addToCartPage;
    private ViewCartPage viewCartPage;

    private AddToCartPage inventoryPage() {
        if (addToCartPage == null) {
            addToCartPage = new AddToCartPage();
        }
        return addToCartPage;
    }

    private ViewCartPage cartPage() {
        if (viewCartPage == null) {
            viewCartPage = new ViewCartPage();
        }
        return viewCartPage;
    }

    @Given("the user has added {string} to the cart")
    public void theUserHasAddedToTheCart(String productName) {
        inventoryPage().addToCart(productName);
    }

    @When("the user navigates to the cart page")
    public void theUserNavigatesToTheCartPage() {
        inventoryPage().goToCart();
    }

    @Then("the cart page should be displayed")
    public void theCartPageShouldBeDisplayed() {
        assertThat(cartPage().isCartPageDisplayed())
            .as("Expected the cart page to be displayed")
            .isTrue();
    }

    @Then("the cart should contain {string}")
    public void theCartShouldContain(String productName) {
        assertThat(cartPage().isItemPresentInCart(productName))
            .as("Expected '" + productName + "' to be present in the cart")
            .isTrue();
    }

    @Then("the cart should contain {int} item(s)")
    public void theCartShouldContainItems(int expectedCount) {
        assertThat(cartPage().getCartItemCount())
            .as("Expected cart to contain the given number of items")
            .isEqualTo(expectedCount);
    }

    @When("the user removes {string} from the cart page")
    public void theUserRemovesFromTheCartPage(String productName) {
        cartPage().removeItemFromCart(productName);
    }

    @Then("the cart should be empty")
    public void theCartShouldBeEmpty() {
        assertThat(cartPage().isCartEmpty())
            .as("Expected the cart to have no items")
            .isTrue();
    }

    @When("the user clicks continue shopping")
    public void theUserClicksContinueShopping() {
        cartPage().clickContinueShopping();
    }

    @Then("the user should be redirected to the inventory page from the cart")
    public void theUserShouldBeRedirectedToTheInventoryPageFromTheCart() {
        assertThat(inventoryPage().getTotalProductCount())
            .as("Expected to be back on the inventory page with products listed")
            .isGreaterThan(0);
    }

    @And("the user clicks checkout from the cart page")
    public void theUserClicksCheckoutFromTheCartPage() {
        cartPage().clickCheckout();
    }
}

