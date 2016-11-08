Feature: as a salesman I want to sell and invoice services to B2B customer in other EU countries

  Background:
    Given A company in "NL" with vat calculation policy is "VAT_CALCULATION_PER_LINE"
    And the company has VAT id "NL0123456789B01" in "NL"
    And An invoiceline worth "100.00" euro "incl" VAT with "High" vat level and referencedate is "2016-01-01"
    And An invoiceline worth "100.00" euro "incl" VAT with "High" vat level and referencedate is "2016-01-01"
    And An invoiceline worth "100.00" euro "incl" VAT with "High" vat level and referencedate is "2016-01-01"
    And An invoiceline worth "100.00" euro "incl" VAT with "High" vat level and referencedate is "2016-01-01"
    And An invoiceline worth "100.00" euro "incl" VAT with "High" vat level and referencedate is "2016-01-01"
    And An invoiceline worth "100.00" euro "incl" VAT with "High" vat level and referencedate is "2016-01-01"
    And An invoiceline worth "106.00" euro "incl" VAT with "Low1" vat level and referencedate is "2016-01-01"
    And An invoiceline worth "100.00" euro "incl" VAT with "Zero" vat level and referencedate is "2016-01-01"

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
    And The VAT amount for percentage "<vatPercentage>" is "<amountVat>" for VAT, "<amountExVat>" for exVAT and "<amountInVat>" for inVAT

    Examples:
      | totalAmountInclVat | totalAmountExVat | totalAmountVat | vatPercentage | amountVat | amountExVat | amountInVat |
      | 806.00             | 695.84           | 110.16         | 21.00         | 104.16    | 495.84      | 600.00      |
      | 806.00             | 695.84           | 110.16         | 6.00          | 6.00      | 100.00      | 106.00      |
      | 806.00             | 695.84           | 110.16         | 0.00          | 0.00      | 100.00      | 100.00      |

  Scenario: Invoice services (NL -> DE) with VAT shifted
    Given the company has VAT id "DE0123456789B01" in "DE"
    And A customer with VAT id "DE67890" and default country is "DE"
    And Country of origin is "NL"
    And Country of destination is "DE"
    And The product category is "SERVICES"
    And Vat is shifted
    When A "consumer" invoice is created at "2016-01-01"
    Then The total amount including VAT is "695.84"
    And The total amount excluding VAT is "695.84"
    And The total amount VAT is "0.00"
    And There are no VAT subtotal lines

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
    And The VAT amount for percentage "<vatPercentage>" is "<amountVat>" for VAT, "<amountExVat>" for exVAT and "<amountInVat>" for inVAT

    Examples:
      | totalAmountInclVat | totalAmountExVat | totalAmountVat | vatPercentage | amountVat | amountExVat | amountInVat |
      | 806.00             | 703.25           | 102.75         | 19.00         | 95.82     | 504.18      | 600.00      |
      | 806.00             | 703.25           | 102.75         | 7.00          | 6.93      | 99.07       | 106.00      |
      | 806.00             | 703.25           | 102.75         | 0.00          | 0.00      | 100.00      | 100.00      |

  Scenario: Invoice E-services (NL -> DE) with VAT shifted
    Given the company has VAT id "DE0123456789B01" in "DE"
    And A customer with VAT id "DE67890" and default country is "DE"
    And Country of origin is "NL"
    And Country of destination is "DE"
    And The product category is "E_SERVICES"
    And Vat is shifted
    When A "consumer" invoice is created at "2016-01-01"
    Then The total amount including VAT is "703.25"
    And The total amount excluding VAT is "703.25"
    And The total amount VAT is "0.00"
    And There are no VAT subtotal lines

