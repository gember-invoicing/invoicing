Feature: As a salesman I want to sell and invoice to local B2B and B2C customers

  Background:
    Given A company in "NL" with vat calculation policy is "VAT_CALCULATION_ON_TOTAL"
    And the company has VAT id "NL0123456789B01" in "NL"
    And An invoiceline worth "100.00" euro "excl" VAT with "High" vat level and referencedate is "2016-01-01"
    And An invoiceline worth "100.00" euro "excl" VAT with "Low1" vat level and referencedate is "2016-01-01"
    And An invoiceline worth "100.00" euro "excl" VAT with "Zero" vat level and referencedate is "2016-01-01"

  Scenario Outline: Deliver goods in primary country to a B2B customer (NL > NL)
    Given A customer with VAT id "NL67890" and default country is "NL"
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

  Scenario Outline: Deliver goods in a secondary EU country with vat registration to a local B2B customer (BE > BE)
    Given A customer with VAT id "BE67890" and default country is "BE"
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
      | 325.00             | 300.00           | 25.00          | 19.00         | 19.00     |
      | 325.00             | 300.00           | 25.00          | 6.00          | 6.00      |
      | 325.00             | 300.00           | 25.00          | 0.00          | 0.00      |

  Scenario Outline: Deliver goods in a secondary EU country with vat registration to a local B2B customer (DE > DE)
    Given A customer with VAT id "DE67890" and default country is "DE"
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
      | 326.00             | 300.00           | 26.00          | 19.00         | 19.00     |
      | 326.00             | 300.00           | 26.00          | 7.00          | 7.00      |
      | 326.00             | 300.00           | 26.00          | 0.00          | 0.00      |

