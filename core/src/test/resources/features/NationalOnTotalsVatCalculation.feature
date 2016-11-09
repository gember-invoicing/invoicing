Feature: As a salesman I want to sell and invoice to local B2C customers with on total calculation

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

  Scenario Outline: Deliver goods in primary country (NL > NL) on totals
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
      | 806.00             | 695.87           | 110.13         | 21.00         | 104.13    | 495.87      | 600.00      |
      | 806.00             | 695.87           | 110.13         | 6.00          | 6.00      | 100.00      | 106.00      |
      | 806.00             | 695.87           | 110.13         | 0.00          | 0.00      | 100.00      | 100.00      |

  Scenario Outline: Deliver goods in primary country (NL > NL) business invoice
    Given A customer without a validated VAT id and default country is "NL"
    And Country of origin is "NL"
    And Country of destination is "NL"
    When A "business" invoice is created at "2016-01-01"
    Then The total amount including VAT is "<totalAmountInclVat>"
    And The total amount excluding VAT is "<totalAmountExVat>"
    And The total amount VAT is "<totalAmountVat>"
    And The VAT amount for percentage "<vatPercentage>" is "<amountVat>" for VAT, "<amountExVat>" for exVAT and "<amountInVat>" for inVAT

    Examples:
      | totalAmountInclVat | totalAmountExVat | totalAmountVat | vatPercentage | amountVat | amountExVat | amountInVat |
      | 805.97             | 695.84           | 110.13         | 21.00         | 104.13    | 495.84      | 599.97      |
      | 805.97             | 695.84           | 110.13         | 6.00          | 6.00      | 100.00      | 106.00      |
      | 805.97             | 695.84           | 110.13         | 0.00          | 0.00      | 100.00      | 100.00      |



  Scenario Outline: Deliver goods in a secondary EU country with vat registration to a local B2C customer (BE > BE) on totals
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
      | 806.00             | 704.20           | 101.80         | 19.00         | 95.80     | 504.20      | 600.00      |
      | 806.00             | 704.20           | 101.80         | 6.00          | 6.00      | 100.00      | 106.00      |
      | 806.00             | 704.20           | 101.80         | 0.00          | 0.00      | 100.00      | 100.00      |

  Scenario Outline: Deliver goods in a secondary EU country with vat registration to a local B2C customer (DE > DE) on totals
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
      | 806.00             | 703.27           | 102.73         | 19.00         | 95.80     | 504.20      | 600.00      |
      | 806.00             | 703.27           | 102.73         | 7.00          | 6.93      | 99.07       | 106.00      |
      | 806.00             | 703.27           | 102.73         | 0.00          | 0.00      | 100.00      | 100.00      |

  Scenario Outline: Deliver goods in primary country to a B2B customer (NL > NL) on totals
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
      | 806.00             | 695.87           | 110.13         | 21.00         | 104.13    | 495.87      | 600.00      |
      | 806.00             | 695.87           | 110.13         | 6.00          | 6.00      | 100.00      | 106.00      |
      | 806.00             | 695.87           | 110.13         | 0.00          | 0.00      | 100.00      | 100.00      |

  Scenario Outline: Deliver goods in a secondary EU country with vat registration to a local B2B customer (BE > BE) on totals
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
      | 806.00             | 704.20           | 101.80         | 19.00         | 95.80     | 504.20      | 600.00      |
      | 806.00             | 704.20           | 101.80         | 6.00          | 6.00      | 100.00      | 106.00      |
      | 806.00             | 704.20           | 101.80         | 0.00          | 0.00      | 100.00      | 100.00      |

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
      | 806.00             | 703.27           | 102.73         | 19.00         | 95.80     | 504.20      | 600.00      |
      | 806.00             | 703.27           | 102.73         | 7.00          | 6.93      | 99.07       | 106.00      |
      | 806.00             | 703.27           | 102.73         | 0.00          | 0.00      | 100.00      | 100.00      |

