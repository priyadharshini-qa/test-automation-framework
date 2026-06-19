package com.saucedemo.stepdefinitions;

import com.saucedemo.config.ConfigReader;
import com.saucedemo.pages.AddToCartPage;
import com.saucedemo.utils.ExcelReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AddToCartSteps {

    private AddToCartPage addToCartPage;
    private String lastAddedProduct;
    private int productsAddedCount;

    private AddToCartPage page() {
        if (addToCartPage == null) {
            addToCartPage = new AddToCartPage();
        }
        return addToCartPage;
    }

    private List<String> getProductNamesFromTestData() {
        String filePath = ConfigReader.get("cartItemsDataFile");
        String sheetName = ConfigReader.get("cartItemsSheetName");
        List<String> productNames = ExcelReader.readColumn(filePath, sheetName, "productName");
        assertThat(productNames)
            .as("Expected " + filePath + " to contain at least one product")
            .isNotEmpty();
        return productNames;
    }

    @When("the user adds the first product from test data to the cart")
    public void theUserAddsTheFirstProductFromTestDataToTheCart() {
        lastAddedProduct = getProductNamesFromTestData().get(0);
        page().addToCart(lastAddedProduct);
    }

    @When("the user adds each product from the test data file to the cart")
    public void theUserAddsEachProductFromTheTestDataFileToTheCart() {
        List<String> productNames = getProductNamesFromTestData();
        for (String productName : productNames) {
            page().addToCart(productName);
        }
        productsAddedCount = productNames.size();
    }

    @And("the user removes that same product from the cart")
    public void theUserRemovesThatSameProductFromTheCart() {
        assertThat(lastAddedProduct)
            .as("No product was previously added in this scenario")
            .isNotNull();
        page().removeFromCart(lastAddedProduct);
    }

    @Then("the cart badge should show {string} item")
    public void theCartBadgeShouldShowItem(String expectedCount) {
        assertThat(page().getCartCount())
            .as("Expected cart badge count to match")
            .isEqualTo(Integer.parseInt(expectedCount));
    }

    @Then("the cart badge should show the same count as the number of products added")
    public void theCartBadgeShouldShowSameCountAsProductsAdded() {
        assertThat(page().getCartCount())
            .as("Expected cart badge to equal number of products added from test data")
            .isEqualTo(productsAddedCount);
    }

    @Then("the cart badge should not be visible")
    public void theCartBadgeShouldNotBeVisible() {
        assertThat(page().isCartBadgeVisible())
            .as("Expected cart badge to be hidden after removing the only item")
            .isFalse();
    }

    @Then("the {string} button should be visible for that product")
    public void theButtonShouldBeVisibleForThatProduct(String buttonLabel) {
        assertThat(lastAddedProduct)
            .as("No product was previously added in this scenario")
            .isNotNull();

        if (buttonLabel.equalsIgnoreCase("Remove")) {
            assertThat(page().isRemoveButtonVisible(lastAddedProduct))
                .as("Expected Remove button to be visible for " + lastAddedProduct)
                .isTrue();
        } else {
            assertThat(page().isAddToCartButtonVisible(lastAddedProduct))
                .as("Expected Add to Cart button to be visible for " + lastAddedProduct)
                .isTrue();
        }
    }
}
