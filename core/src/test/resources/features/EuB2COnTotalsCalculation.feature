Feature: As a salesman I want to deliver goods and services to customers in other EU countries

  Background:
    Given A company in "NL" with vat calculation policy is "VAT_CALCULATION_ON_TOTAL"
    And the company has VAT id "NL0123456789B01" in "NL"
    And An invoiceline worth "100.00" euro "incl" VAT with "High" vat level and referencedate is "2016-01-01"
    And An invoiceline worth "100.00" euro "incl" VAT with "High" vat level and referencedate is "2016-01-01"
    And An invoiceline worth "100.00" euro "incl" VAT with "High" vat level and referencedate is "2016-01-01"
    And An invoiceline worth "100.00" euro "incl" VAT with "High" vat level and referencedate is "2016-01-01"
    And An invoiceline worth "100.00" euro "incl" VAT with "High" vat level and referencedate is "2016-01-01"
    And An invoiceline worth "100.00" euro "incl" VAT with "High" vat level and referencedate is "2016-01-01"
    And An invoiceline worth "106.00" euro "incl" VAT with "Low1" vat level and referencedate is "2016-01-01"
    And An invoiceline worth "100.00" euro "incl" VAT with "Zero" vat level and referencedate is "2016-01-01"

  Scenario Outline: Invoice goods to a customer in another EU country (NL > DE) on totals
    Given the company has VAT id "DE0123456789B01" in "DE"
    And A customer without a validated VAT id and default country is "NL"
    And Country of origin is "NL"
    And Country of destination is "DE"
    When A "consumer" invoice is created at "2016-01-01"
    Then The total amount including VAT is "<totalAmountInclVat>"
    And The total amount excluding VAT is "<totalAmountExVat>"
    And The total amount VAT is "<totalAmountVat>"
    And The VAT amount for percentage "<vatPercentage>" is "<amountVat>" for VAT, "<amountExVat>" for exVAT and "<amountInVat>" for inVAT

    Examples:
      | totalAmountInclVat | totalAmountExVat | totalAmountVat | vatPercentage | amountVat | amountExVat | amountInVat |
      | 806.00             | 703.27           | 102.73         | 19.00         | 95.80     | 504.20      | 600.00      |
      | 806.00             | 703.27           | 102.73         | 7.00          | 6.93      | 99.07       | 106.00      |
      | 806.00             | 703.27           | 102.73         | 0.00          | 0.00      | 100.00      | 100.00      |

  Scenario Outline: Invoice goods to a customer in another EU country (NL > DE) on totals, business
    Given the company has VAT id "DE0123456789B01" in "DE"
    And A customer without a validated VAT id and default country is "NL"
    And Country of origin is "NL"
    And Country of destination is "DE"
    When A "business" invoice is created at "2016-01-01"
    Then The total amount including VAT is "<totalAmountInclVat>"
    And The total amount excluding VAT is "<totalAmountExVat>"
    And The total amount VAT is "<totalAmountVat>"
    And The VAT amount for percentage "<vatPercentage>" is "<amountVat>" for VAT, "<amountExVat>" for exVAT and "<amountInVat>" for inVAT

    Examples:
      | totalAmountInclVat | totalAmountExVat | totalAmountVat | vatPercentage | amountVat | amountExVat | amountInVat |
      | 805.97             | 703.25           | 102.72         | 19.00         | 95.79     | 504.18      | 599.97      |
      | 805.97             | 703.25           | 102.72         | 7.00          | 6.93      | 99.07       | 106.00      |
      | 805.97             | 703.25           | 102.72         | 0.00          | 0.00      | 100.00      | 100.00      |



  Scenario Outline: Invoice goods to a B2C customer in another EU country (BE > DE) on totals
    Given the company has VAT id "BE0123456789B01" in "BE"
    And the company has VAT id "DE0123456789B01" in "DE"
    And A customer without a validated VAT id and default country is "NL"
    And Country of origin is "BE"
    And Country of destination is "DE"
    When A "consumer" invoice is created at "2016-01-01"
    Then The total amount including VAT is "<totalAmountInclVat>"
    And The total amount excluding VAT is "<totalAmountExVat>"
    And The total amount VAT is "<totalAmountVat>"
    And The VAT amount for percentage "<vatPercentage>" is "<amountVat>" for VAT, "<amountExVat>" for exVAT and "<amountInVat>" for inVAT

    Examples:
      | totalAmountInclVat | totalAmountExVat | totalAmountVat | vatPercentage | amountVat | amountExVat | amountInVat |
      | 806.00             | 703.27           | 102.73         | 19.00         | 95.80     | 504.20      | 600.00      |
      | 806.00             | 703.27           | 102.73         | 7.00          | 6.93      | 99.07       | 106.00      |
      | 806.00             | 703.27           | 102.73         | 0.00          | 0.00      | 100.00      | 100.00      |

  Scenario Outline: Invoice goods to a B2C customer in another EU country (BE > DE) on totals, business
    Given the company has VAT id "BE0123456789B01" in "BE"
    And the company has VAT id "DE0123456789B01" in "DE"
    And A customer without a validated VAT id and default country is "NL"
    And Country of origin is "BE"
    And Country of destination is "DE"
    When A "business" invoice is created at "2016-01-01"
    Then The total amount including VAT is "<totalAmountInclVat>"
    And The total amount excluding VAT is "<totalAmountExVat>"
    And The total amount VAT is "<totalAmountVat>"
    And The VAT amount for percentage "<vatPercentage>" is "<amountVat>" for VAT, "<amountExVat>" for exVAT and "<amountInVat>" for inVAT

    Examples:
      | totalAmountInclVat | totalAmountExVat | totalAmountVat | vatPercentage | amountVat | amountExVat | amountInVat |
      | 805.97             | 703.25           | 102.72         | 19.00         | 95.79     | 504.18      | 599.97      |
      | 805.97             | 703.25           | 102.72         | 7.00          | 6.93      | 99.07       | 106.00      |
      | 805.97             | 703.25           | 102.72         | 0.00          | 0.00      | 100.00      | 100.00      |



  Scenario Outline: Invoice goods to a B2C customer in another EU country (BE > NL) on totals and destination is not given
    Given the company has VAT id "BE0123456789B01" in "BE"
    And A customer without a validated VAT id and default country is "NL"
    And Country of origin is "BE"
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

  Scenario Outline: Invoice goods to a customer in another EU country (NL > DE) on toals and company has no registration in destination country
    Given A customer without a validated VAT id and default country is "NL"
    And Country of origin is "NL"
    And Country of destination is "DE"
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

