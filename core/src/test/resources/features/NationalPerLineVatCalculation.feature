Feature: As a salesman I want to sell and invoice to local customers with Per Line Calculation

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

  Scenario Outline: Deliver goods in primary country (NL > NL)
    Given A customer without a validated VAT id and default country is "NL"
    And Country of origin is "NL"
    And Country of destination is "NL"
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

  Scenario Outline: Deliver goods in a secondary EU country with vat registration to a local B2C customer (BE > BE)
    Given A customer without a validated VAT id and default country is "BE"
    And the company has VAT id "BE12345" in "BE"
    And Country of origin is "BE"
    And Country of destination is "BE"
    When A "consumer" invoice is created at "2016-01-01"
    Then The total amount including VAT is "<totalAmountInclVat>"
    And The total amount excluding VAT is "<totalAmountExVat>"
    And The total amount VAT is "<totalAmountVat>"
    And The VAT amount for percentage "<vatPercentage>" is "<amountVat>" for VAT, "<amountExVat>" for exVAT and "<amountInVat>" for inVAT

    Examples:
      | totalAmountInclVat | totalAmountExVat | totalAmountVat | vatPercentage | amountVat | amountExVat | amountInVat |
      | 806.00             | 704.18           | 101.82         | 19.00         | 95.82     | 504.18      | 600.00      |
      | 806.00             | 704.18           | 101.82         | 6.00          | 6.00      | 100.00      | 106.00      |
      | 806.00             | 704.18           | 101.82         | 0.00          | 0.00      | 100.00      | 100.00      |

  Scenario Outline: Deliver goods in a secondary EU country with vat registration to a local B2C customer (DE > DE)
    Given A customer without a validated VAT id and default country is "NL"
    And the company has VAT id "DE12345" in "DE"
    And Country of origin is "DE"
    And Country of destination is "DE"
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

  Scenario Outline: Deliver goods in primary country to a B2B customer (NL > NL)
    Given A customer with VAT id "NL67890" and default country is "NL"
    And Country of origin is "NL"
    And Country of destination is "NL"
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

  Scenario Outline: Deliver goods in a secondary EU country with vat registration to a local B2B customer (BE > BE)
    Given A customer with VAT id "BE67890" and default country is "BE"
    And the company has VAT id "BE12345" in "BE"
    And Country of origin is "BE"
    And Country of destination is "BE"
    When A "consumer" invoice is created at "2016-01-01"
    Then The total amount including VAT is "<totalAmountInclVat>"
    And The total amount excluding VAT is "<totalAmountExVat>"
    And The total amount VAT is "<totalAmountVat>"
    And The VAT amount for percentage "<vatPercentage>" is "<amountVat>" for VAT, "<amountExVat>" for exVAT and "<amountInVat>" for inVAT

    Examples:
      | totalAmountInclVat | totalAmountExVat | totalAmountVat | vatPercentage | amountVat | amountExVat | amountInVat |
      | 806.00             | 704.18           | 101.82         | 19.00         | 95.82     | 504.18      | 600.00      |
      | 806.00             | 704.18           | 101.82         | 6.00          | 6.00      | 100.00      | 106.00      |
      | 806.00             | 704.18           | 101.82         | 0.00          | 0.00      | 100.00      | 100.00      |

  Scenario Outline: Deliver goods in a secondary EU country with vat registration to a local B2B customer (DE > DE)
    Given A customer with VAT id "DE67890" and default country is "DE"
    And the company has VAT id "DE12345" in "DE"
    And Country of origin is "DE"
    And Country of destination is "DE"
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

  Scenario: Deliver goods in primary country to a B2B customer (NL > NL) under VAT Shifted policy
    Given A company in "NL" with vat calculation policy is "VAT_CALCULATION_PER_LINE"
    And the company has VAT id "NL0123456789B01" in "NL"
    Given A customer with VAT id "NL67890" and default country is "NL"
    And Country of origin is "NL"
    And Country of destination is "NL"
    And Vat is shifted
    When A "consumer" invoice is created at "2016-01-01"
    Then The total amount including VAT is "695.84"
    And The total amount excluding VAT is "695.84"
    And The total amount VAT is "0.00"
    And There are no VAT subtotal lines

  Scenario: A good is delivered from the companies default country
    Given A customer without a validated VAT id and default country is "NL"
    And Country of destination is "NL"
    When A "consumer" invoice is created at "2016-01-01"
    Then The total amount including VAT is "806.00"
    And The total amount excluding VAT is "695.84"
    And The total amount VAT is "110.16"
    And The VAT amount for percentage "21.00" is "104.16" for VAT, "495.84" for exVAT and "600.00" for inVAT

  Scenario: A good is delivered from the companies default country and no default country is set
    Given A company in "" with vat calculation policy is "VAT_CALCULATION_PER_LINE"
    And the company has VAT id "NL0123456789B01" in "NL"
    And A customer without a validated VAT id and default country is ""
    And Country of destination is "NL"
    When A "consumer" invoice is created at "2016-01-01"
    Then The invoice calculation request throws an no origin country set exception

  Scenario: A good is created for an unknown vat percentage
    Given A customer without a validated VAT id and default country is "NL"
    And An invoiceline worth "121.00" euro "incl" VAT with "High" vat level and referencedate is "1950-01-01"
    And Country of origin is "NL"
    And Country of destination is "NL"
    When A "consumer" invoice is created at "2016-01-01"
    Then The invoice calculation request throws an vat percentage not found exception

  Scenario: Deliver goods in a secondary EU country without vat registration to a local B2C customer
    Given A customer without a validated VAT id and default country is "BE"
    And Country of origin is "BE"
    And Country of destination is "BE"
    When A "consumer" invoice is created at "2016-01-01"
    Then The invoice calculation request throws an no registration in origin country exception
