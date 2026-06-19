Feature: Add to Cart functionality
  As a logged-in SauceDemo user
  I want to add products to my cart
  So that I can purchase the items I want

  @smoke @regression
  Scenario Outline: Add a single product to the cart after login
    Given the user is on the login page
    And the user logs in with username "<username>" and password "<password>"
    When the user adds the first product from test data to the cart
    Then the cart badge should show "1" item

    Examples:
      | username      | password     |
      | standard_user | secret_sauce |

  @regression
  Scenario Outline: Add every product listed in test data to the cart
    Given the user is on the login page
    And the user logs in with username "<username>" and password "<password>"
    When the user adds each product from the test data file to the cart
    Then the cart badge should show the same count as the number of products added

    Examples:
      | username      | password     |
      | standard_user | secret_sauce |

  @regression
  Scenario Outline: Remove a product after adding it
    Given the user is on the login page
    And the user logs in with username "<username>" and password "<password>"
    When the user adds the first product from test data to the cart
    And the user removes that same product from the cart
    Then the cart badge should not be visible

    Examples:
      | username      | password     |
      | standard_user | secret_sauce |

  @regression
  Scenario Outline: Add to Cart button changes to Remove after adding
    Given the user is on the login page
    And the user logs in with username "<username>" and password "<password>"
    When the user adds the first product from test data to the cart
    Then the "Remove" button should be visible for that product

    Examples:
      | username      | password     |
      | standard_user | secret_sauce |