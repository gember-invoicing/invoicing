Feature: as a salesman I want to sell and invoice services to B2B customer in other EU countries

  Background:
    Given A company in "NL" with vat calculation policy is "VAT_CALCULATION_ON_TOTAL"
    And the company has VAT id "NL0123456789B01" in "NL"
    And An invoiceline worth "100.00" euro "excl" VAT with "High" vat level and referencedate is "2016-01-01"
    And An invoiceline worth "100.00" euro "excl" VAT with "Low1" vat level and referencedate is "2016-01-01"
    And An invoiceline worth "100.00" euro "excl" VAT with "Zero" vat level and referencedate is "2016-01-01"

  Scenario Outline: Invoice services (NL -> DE)
    Given the company has VAT id "DE0123456789B01" in "DE"
    And A customer with VAT id "DE67890" and default country is "DE"
    And Country of origin is "NL"
    And Country of destination is "DE"
    And The product category is "SERVICES"
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

  Scenario: Invoice services (NL -> DE) with VAT shifted
    Given the company has VAT id "DE0123456789B01" in "DE"
    And A customer with VAT id "DE67890" and default country is "DE"
    And Country of origin is "NL"
    And Country of destination is "DE"
    And The product category is "SERVICES"
    And Vat is shifted
    When A "consumer" invoice is created at "2016-01-01"
    Then The total amount including VAT is "300.00"
    And The total amount excluding VAT is "300.00"
    And The total amount VAT is "0.00"
    And Invoice is attributed as VAT Shifted

  Scenario Outline: Invoice E-services (NL -> DE)
    Given the company has VAT id "DE0123456789B01" in "DE"
    And A customer with VAT id "DE67890" and default country is "DE"
    And Country of origin is "NL"
    And Country of destination is "DE"
    And The product category is "E_SERVICES"
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

  Scenario: Invoice E-services (NL -> DE) with VAT shifted
    Given the company has VAT id "DE0123456789B01" in "DE"
    And A customer with VAT id "DE67890" and default country is "DE"
    And Country of origin is "NL"
    And Country of destination is "DE"
    And The product category is "E_SERVICES"
    And Vat is shifted
    When A "consumer" invoice is created at "2016-01-01"
    Then The total amount including VAT is "300.00"
    And The total amount excluding VAT is "300.00"
    And The total amount VAT is "0.00"
    And Invoice is attributed as VAT Shifted

