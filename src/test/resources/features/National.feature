Feature: As a salesman I want to sell and invoice to local B2C customers

  Background:
    Given A company in "NL" with vat calculation policy is "VAT_CALCULATION_ON_TOTAL"
    And the company has VAT id "NL0123456789B01" in "NL"
    And An invoiceline worth "121.00" euro "incl" VAT with "High" vat level and referencedate is "2016-01-01"
    And An invoiceline worth "106.00" euro "incl" VAT with "Low1" vat level and referencedate is "2016-01-01"
    And An invoiceline worth "100.00" euro "incl" VAT with "Zero" vat level and referencedate is "2016-01-01"


  Scenario Outline: Deliver
    #Levering NL > NL
    Given A customer without a validated VAT id and default country is "NL"
    And Country of origin is "NL"
    And Country of destination is "NL"
    When A "consumer" invoice is created at "2016-01-01"
    Then The total amount including VAT is "<totalAmountInclVat>"
    And The total amount excluding VAT is "<totalAmountExVat>"
    And The total amount VAT is "<totalAmountVat>"
    And The VAT amount for percentage "<vatPercentage>" is "<amountVat>"

    Examples:
      | totalAmountInclVat | totalAmountExVat | totalAmountVat | vatPercentage | amountVat |
      | 327.00             | 300.00           | 27.00          | 21.00         | 21.00     |
      | 327.00             | 300.00           | 27.00          | 6.00          | 6.00      |
      | 327.00             | 300.00           | 27.00          | 0.00          | 0.00      |

  Scenario Outline: Deliver goods in a secondary EU country with vat registration to a local B2C customer
    #Levering BE > BE
    Given A customer without a validated VAT id and default country is "BE"
    And the company has VAT id "BE12345" in "BE"
    And Country of origin is "BE"
    And Country of destination is "BE"
    When A "consumer" invoice is created at "2016-01-01"
    Then The total amount including VAT is "<totalAmountInclVat>"
    And The total amount excluding VAT is "<totalAmountExVat>"
    And The total amount VAT is "<totalAmountVat>"
    And The VAT amount for percentage "<vatPercentage>" is "<amountVat>"

    Examples:
      | totalAmountInclVat | totalAmountExVat | totalAmountVat | vatPercentage | amountVat |
      | 327.00             | 301.68           | 25.32          | 19.00         | 19.32     |
      | 327.00             | 301.68           | 25.32          | 6.00          | 6.00      |
      | 327.00             | 301.68           | 25.32          | 0.00          | 0.00      |

  Scenario Outline: Deliver goods in a secondary EU country with vat registration to a local B2C customer
    #Levering DE > DE
    Given A customer without a validated VAT id and default country is "NL"
    And the company has VAT id "DE12345" in "DE"
    And Country of origin is "DE"
    And Country of destination is "DE"
    When A "consumer" invoice is created at "2016-01-01"
    Then The total amount including VAT is "<totalAmountInclVat>"
    And The total amount excluding VAT is "<totalAmountExVat>"
    And The total amount VAT is "<totalAmountVat>"
    And The VAT amount for percentage "<vatPercentage>" is "<amountVat>"

    Examples:
      | totalAmountInclVat | totalAmountExVat | totalAmountVat | vatPercentage | amountVat |
      | 327.00             | 300.75           | 26.25          | 19.00         | 19.32     |
      | 327.00             | 300.75           | 26.25          | 7.00          | 6.93      |
      | 327.00             | 300.75           | 26.25          | 0.00          | 0.00      |

  @not_implemented
  Scenario: A good is delivered from the companies default country

  @not_implemented
  Scenario: A good is delivered from the companies default country and no default country is set

  Scenario Outline: A good is delivered to the customer default country
    Given A customer without a validated VAT id and default country is "NL"
    And Country of destination is "NL"
    When A "consumer" invoice is created at "2016-01-01"
    Then The total amount including VAT is "<totalAmountInclVat>"
    And The total amount excluding VAT is "<totalAmountExVat>"
    And The total amount VAT is "<totalAmountVat>"
    And The VAT amount for percentage "<vatPercentage>" is "<amountVat>"

  Examples:
  | totalAmountInclVat | totalAmountExVat | totalAmountVat | vatPercentage | amountVat |
  | 327.00             | 300.00           | 27.00          | 21.00         | 21.00     |

  Scenario Outline: A good is delivered to the customer default country and no default country is set
    Given A customer without a validated VAT id and default country is ""
    And Country of destination is "NL"
    When A "consumer" invoice is created at "2016-01-01"
    Then The total amount including VAT request throws an no origin country set exception
    Then The total amount excluding VAT request throws an no origin country set exception
    Then The total amount VAT request throws an no origin country set exception

  Examples:
  | totalAmountInclVat | totalAmountExVat | totalAmountVat | vatPercentage | amountVat |
  | 327.00             | 300.00           | 27.00          | 21.00         | 21.00     |


  @not_implemented
  Scenario: A good is created for an unknown vat percentage

  @not_implemented
  Scenario: Deliver goods in a secondary EU country without vat registration to a local B2C customer
    #Levering BE > BE; geen vat registratie in BE
    Given A customer without a validated VAT id and default country is "BE"
    And Country of origin is "BE"
    And Country of destination is "BE"
    When A "consumer" invoice is created at "2016-01-01"
    Then The total amount including VAT request throws an no registration in origin country exception
    Then The total amount excluding VAT request throws an no registration in origin country exception
    Then The total amount VAT request throws an no registration in origin country exception

