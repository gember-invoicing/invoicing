Feature: As a salesman I want to deliver goods and services to customers in other EU countries

  Background:
    Given A company in "NL" with vat calculation policy is "VAT_CALCULATION_PER_LINE"
    And the company has VAT id "NL0123456789B01" in "NL"
    And An invoiceline worth "100.00" euro "excl" VAT with "High" vat level and referencedate is "2016-01-01"
    And An invoiceline worth "100.00" euro "excl" VAT with "Low1" vat level and referencedate is "2016-01-01"
    And An invoiceline worth "100.00" euro "excl" VAT with "Zero" vat level and referencedate is "2016-01-01"

  Scenario Outline: Invoice goods to a customer in another EU country (NL > DE)
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
      | 326.00             | 300.00           | 26.00          | 19.00         | 19.00     | 100.00      | 119.00      |
      | 326.00             | 300.00           | 26.00          | 7.00          | 7.00      | 100.00      | 107.00      |
      | 326.00             | 300.00           | 26.00          | 0.00          | 0.00      | 100.00      | 100.00      |

  Scenario Outline: Invoice goods to a B2C customer in another EU country (BE > DE)
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
      | 326.00             | 300.00           | 26.00          | 19.00         | 19.00     | 100.00      | 119.00      |
      | 326.00             | 300.00           | 26.00          | 7.00          | 7.00      | 100.00      | 107.00      |
      | 326.00             | 300.00           | 26.00          | 0.00          | 0.00      | 100.00      | 100.00      |

  Scenario Outline: Invoice goods to a B2C customer in another EU country (BE > NL) and destination is not given
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
      | 327.00             | 300.00           | 27.00          | 21.00         | 21.00     | 100.00      | 121.00      |
      | 327.00             | 300.00           | 27.00          | 6.00          | 6.00      | 100.00      | 106.00      |
      | 327.00             | 300.00           | 27.00          | 0.00          | 0.00      | 100.00      | 100.00      |

  Scenario: Invoice goods to a B2C customer in another EU country (BE > NL) and no destination is not given
    Given the company has VAT id "BE0123456789B01" in "BE"
    And A customer without a validated VAT id and default country is ""
    And Country of origin is "BE"
    When A "consumer" invoice is created at "2016-01-01"
    Then The invoice calculation request throws an no destination country set exception

  Scenario Outline: Invoice goods to a customer in another EU country (NL > DE) and company has no registration in destination country
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
      | 327.00             | 300.00           | 27.00          | 21.00         | 21.00     | 100.00      | 121.00      |
      | 327.00             | 300.00           | 27.00          | 6.00          | 6.00      | 100.00      | 106.00      |
      | 327.00             | 300.00           | 27.00          | 0.00          | 0.00      | 100.00      | 100.00      |

