Feature: View Cart functionality
  As a logged-in SauceDemo user
  I want to view and manage items in my cart
  So that I can review my order before checking out

  Background:
    Given the user is on the login page
    And the user logs in with username "standard_user" and password "secret_sauce"

  @smoke @regression
  Scenario: View cart after adding a product
    Given the user has added "Sauce Labs Backpack" to the cart
    When the user navigates to the cart page
    Then the cart page should be displayed
    And the cart should contain "Sauce Labs Backpack"
    And the cart should contain 1 item

  @regression
  Scenario: Cart reflects multiple added products
    Given the user has added "Sauce Labs Backpack" to the cart
    And the user has added "Sauce Labs Bike Light" to the cart
    When the user navigates to the cart page
    Then the cart page should be displayed
    And the cart should contain "Sauce Labs Backpack"
    And the cart should contain "Sauce Labs Bike Light"
    And the cart should contain 2 items

  @regression
  Scenario: Remove an item from the cart page
    Given the user has added "Sauce Labs Backpack" to the cart
    When the user navigates to the cart page
    And the user removes "Sauce Labs Backpack" from the cart page
    Then the cart should be empty

  @regression
  Scenario: Continue shopping returns to the inventory page
    Given the user has added "Sauce Labs Backpack" to the cart
    When the user navigates to the cart page
    And the user clicks continue shopping
    Then the user should be redirected to the inventory page from the cart

  @smoke @regression
  Scenario: Proceed to checkout from the cart page
    Given the user has added "Sauce Labs Backpack" to the cart
    When the user navigates to the cart page
    And the user clicks checkout from the cart page
    Then the checkout page should be displayed
