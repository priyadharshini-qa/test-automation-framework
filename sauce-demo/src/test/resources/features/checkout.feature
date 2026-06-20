Feature: Checkout functionality
  As a logged-in SauceDemo user
  I want to complete the checkout process
  So that I can purchase the items in my cart

  Background:
    Given the user is on the login page
    And the user logs in with username "standard_user" and password "secret_sauce"
    And the user has added "Sauce Labs Backpack" to the cart
    And the user navigates to the cart page
    And the user clicks checkout from the cart page

  @smoke @regression
  Scenario: Checkout page is displayed after clicking checkout
    Then the checkout page should be displayed

  @smoke @regression
  Scenario Outline: Successful checkout with valid customer details
    When the user enters checkout details "<firstName>" "<lastName>" "<postalCode>"
    And the user continues to the order overview
    Then the order overview page should be displayed
    And the order summary should contain "Sauce Labs Backpack"
    When the user finishes the checkout
    Then the order should be confirmed

    Examples:
      | firstName | lastName | postalCode |
      | John      | Doe      | 12345      |
      | Jane      | Smith    | SW1A 1AA   |

  @regression
  Scenario Outline: Checkout fails when required customer details are missing
    When the user enters checkout details "<firstName>" "<lastName>" "<postalCode>"
    And the user continues to the order overview
    Then a checkout error message should be displayed

    Examples:
      | firstName | lastName | postalCode |
      |           | Doe      | 12345      |
      | John      |          | 12345      |
      | John      | Doe      |            |

  @regression
  Scenario: Cancel from checkout details returns to the cart
    When the user cancels the checkout from the details page
    Then the cart page should be displayed

  @regression
  Scenario: Cancel from order overview returns to the inventory page
    When the user enters checkout details "John" "Doe" "12345"
    And the user continues to the order overview
    And the user cancels the checkout from the overview page
    Then the user should be redirected to the inventory page from the cart

  @regression
  Scenario: Order overview shows correct price summary
    When the user enters checkout details "John" "Doe" "12345"
    And the user continues to the order overview
    Then the order total should equal the subtotal plus tax
