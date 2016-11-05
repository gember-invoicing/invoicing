Feature: as a salesman I want to sell invoice products to countries outside the EU

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

  Scenario Outline: Deliver goods in primary country (NL > NO)
    Given A customer without a validated VAT id and default country is "NL"
    And Country of origin is "NL"
    And Country of destination is "NO"
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

  Scenario Outline: Deliver goods in primary country (NL > NO)
    Given A company in "NL" with vat calculation policy is "VAT_CALCULATION_ON_TOTAL"
    And the company has VAT id "NL0123456789B01" in "NL"
    Given A customer without a validated VAT id and default country is "NL"
    And Country of origin is "NL"
    And Country of destination is "NO"
    When A "consumer" invoice is created at "2016-01-01"
    Then The total amount including VAT is "<totalAmountInclVat>"
    And The total amount excluding VAT is "<totalAmountExVat>"
    And The total amount VAT is "<totalAmountVat>"
    And The VAT amount for percentage "<vatPercentage>" is "<amountVat>" for VAT, "<amountExVat>" for exVAT and "<amountInVat>" for inVAT

    Examples:
      | totalAmountInclVat | totalAmountExVat | totalAmountVat | vatPercentage | amountVat | amountExVat | amountInVat |
      | 806.00             | 695.87           | 110.13         | 21.00         | 104.13    | 495.87      | 600.00      |
      | 806.00             | 695.87           | 110.13         | 6.00          | 6.00      | 100.00      | 106.00      |
      | 806.00             | 695.87           | 110.13         | 0.00          | 0.00      | 100.00      | 100.00      |




#    TODO; klant heeft geldig BTW nummer maar levering is buiten EU. Dus export. Dus BTW berekenen.

  Scenario: Deliver goods in primary country (NL > NO) with VAT shifted
    Given A customer without a validated VAT id and default country is "NL"
    And Country of origin is "NL"
    And Country of destination is "NO"
    And Vat is shifted
    When A "consumer" invoice is created at "2016-01-01"
    Then The total amount including VAT is "695.84"
    And The total amount excluding VAT is "695.84"
    And The total amount VAT is "0.00"
    And There are no VAT subtotal lines

