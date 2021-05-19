# Unit Testing Documentation

Authors:

Date:

Version:

# Contents

- [Black Box Unit Tests](#black-box-unit-tests)




- [White Box Unit Tests](#white-box-unit-tests)


# Black Box Unit Tests

    <Define here criteria, predicates and the combination of predicates for each function of each class.
    Define test cases to cover all equivalence classes and boundary conditions.
    In the table, report the description of the black box test case and (traceability) the correspondence with the JUnit test case writing the 
    class and method name that contains the test case>
    <JUnit test classes must be in src/test/java/it/polito/ezshop   You find here, and you can use,  class TestEzShops.java that is executed  
    to start tests
    >
### **Class *TransactionManager* - method *luhn***



**Criteria for method *luhn*:**


-Sign of number
-Correctness of String
-Presence of non-numerical values
-Emptiness





**Predicates for method *luhn*:**

| Criteria | Predicate |
| -------- | --------- |
|        Sign of number  |    postive       |
|          |   negative        |
|    Correctness of String      |    79927398713       |
|          |      5     |
| Presence of non-numerical values | iduhsidh|
| Emptiness | ""|
| null | null|






**Combination of predicates**:

| Sign of number | Correctness of String | Presence of non-numerical values|Emptiness |null| Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|-------|-------|-------|
|Positive|Correct|No|No|No|valid|luhn("79927398713")-> true|it.polito.ezshop.classes.TransactionMangerTest::**testLuhn**|
|Negative|*|*|*|*|invalid|luhn("-79927398713")|it.polito.ezshop.classes.TransactionMangerTest::**testLuhn**|
|*|Incorrect|*|*|*|Invalid|luhn("5")|it.polito.ezshop.classes.TransactionMangerTest::**testLuhn**|
|*|*|Yes|*|*|Invalid|luhn("iduhsidh")|it.polito.ezshop.classes.TransactionMangerTest::**testLuhn**|
|*|*|*|Yes|*|Invalid|luhn("")|it.polito.ezshop.classes.TransactionMangerTest::**testLuhn**|
|*|*|*|*|Yes|Invalid|luhn(null)|it.polito.ezshop.classes.TransactionMangerTest::**testLuhn**|

### **Class *ProductOrderManager* - method *checkBarcode***

**Criteria for method *checkBarcode*:**

-Length of string -Validity of the String

**Predicates for method *checkBarcode*:**

| Criteria                                            | Predicate          |
| --------------------------------------------------- | ------------------ |
|    Validity of the string                           |       yes          |
|                                                     |       no (null)    |
|    Length of the String parameter                   |       < 12         |
|                                                     |       [ 12,14 ]    |
|                                                     |       > 14         |
| Validity of the String according to the check-digit |       yes          |
|                                                     |       no           |
|    Presence of non-numerical values                 |       yes          |
|                                                     |       no           |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|    Length of the String parameter      |       12         |
|                                        |       14         |

**Combination of predicates**:

| Validity of the string | Presence of non-numerical values | Length of the String parameter | Validity of the String according to the check-digit | Description of the test case                                 | JUnit test case                                              |
| ---------------------- | -------------------------------- | ------------------------------ | --------------------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| null                   | *                                | *                              | *                                                   | T1(null; false)                                              | it.polito.ezshop.classes.ProductOrderManagerTest::testCheckBarcodeNull |
| yes                    | yes                              | *                              | *                                                   | T2("a2bcde!"; false)                                         | it.polito.ezshop.classes.ProductOrderManagerTest::testCheckBarcodeNonNum |
| yes                    | no                               | < 12                           | *                                                   | T3("1234567"; false)                                         | it.polito.ezshop.classes.ProductOrderManagerTest::testCheckBarcodeLenghtLessThan12 |
| yes                    | no                               | > 14                           | *                                                   | T4("123456789012345"; false)                                 | it.polito.ezshop.classes.ProductOrderManagerTest::testCheckBarcodeLenghtGreaterThan14 |
| yes                    | no                               | [ 12,14 ]                      | no                                                  | T5("123456789013"; false)<br />T5b("1234567890137"; false)<br />T5c("12345678901377"; false) | it.polito.ezshop.classes.ProductOrderManagerTest::testCheckBarcodeCheckDigitNotValid |
| yes                    | no                               | [ 12,14 ]                      | yes                                                 | T6("123456789012"; true)<br />T6b("1234567890128"; true)<br />T6c("12345678901286"; true) | it.polito.ezshop.classes.ProductOrderManagerTest::testCheckBarcodeCheckDigitValid |

### **Class *UserObj* - method *setRole***

**Criteria for method *setRole*:**

- Validity of the String parameter

**Predicates for method *setRole*:**

| Criteria | Predicate |
| -------- | --------- |
|    Validity of the String parameter      |     Valid      |
|                                          |      Invalid   |
|                                          |      NULL      |






**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|    Validity of the String parameter      |        "ADMINISTRATOR", "CASHIER", "SHOPMANAGER"        |




**Combination of predicates**:


| Validity of the String parameter | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Valid|Valid|T1("ADMINISTRATOR") -> role set|it.polito.ezshop.classes.UserObjTest::**testSetRoleShopManager**|
|Valid|Valid|T2("CASHIER") -> role set|it.polito.ezshop.classes.UserObjTest::**testSetRoleCashier**|
|Valid|Valid|T3("SHOPMANAGER") -> role set|it.polito.ezshop.classes.UserObjTest::**testSetRoleShopManager**|
|Invalid|Valid|T4("t") -> role not set|it.polito.ezshop.classes.UserObjTest::**testSetRoleInvalidString**|
|NULL|Valid|T5(null) -> role not set|it.polito.ezshop.classes.UserObjTest::**testSetRoleNull**|



# White Box Unit Tests

### Test cases definition

    <JUnit test classes must be in src/test/java/it/polito/ezshop>
    <Report here all the created JUnit test cases, and the units/classes under test >
    <For traceability write the class and method name that contains the test case>

| Unit name | JUnit test case |
|-----------|-----------------| 
|src.main.java.it.polito.ezshop.classes.ProductOrderManager | src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**
testCheckBarcodeNull**|
| | src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testCheckBarcodeNonNum** |              
| | src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testCheckBarcodeLenghtLessThan12** |    
| | src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testCheckBarcodeLenghtGreaterThan14** | 
| | src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testCheckBarcodeCheckDigitNotValid** |  
| | src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testCheckBarcodeCheckDigitValid** |
| src.main.java.it.polito.ezshop.classes.CustomerObj  | src.test.it.polito.ezshop.classes.CustomerObjTest::**testSetLoyaltyCard**|
|   |  src.test.it.polito.ezshop.classes.CustomerObjTest::**testSetCustomerName**|
|   |  src.test.it.polito.ezshop.classes.CustomerObjTest::**testSetCustomerCard**|
|   | src.test.it.polito.ezshop.classes.CustomerObjTest::**testSetId** |
|   | src.test.it.polito.ezshop.classes.CustomerObjTest::**testSetPoints** |
| src.main.java.it.polito.ezshop.classes.LoyaltyCardObj | src.test.it.polito.ezshop.classes.LoyaltyCardObjTest::**testSetIsAttached** |
|   | src.test.it.polito.ezshop.classes.LoyaltyCardObjTest::**testSetCardCode** |
|   |  src.test.it.polito.ezshop.classes.LoyaltyCardObjTest::**testSetPoints**|
|  src.main.java.it.polito.ezshop.classes.UserObj | src.test.it.polito.ezshop.classes.UserObjTest::**testSetId**  |
|   |  src.test.it.polito.ezshop.classes.UserObjTest::**testSetUsername** |
|   |  src.test.it.polito.ezshop.classes.UserObjTest::**testSetPassword** |
|   |  src.test.it.polito.ezshop.classes.UserObjTest::**testSetRoleShopManager**  |
|   | src.test.it.polito.ezshop.classes.UserObjTest::**testSetRoleAdministrator**   |
|   |  src.test.it.polito.ezshop.classes.UserObjTest::**testSetRoleCashier**   |
|   | src.test.it.polito.ezshop.classes.UserObjTest::**testSetRoleInvalidString**   |
|   |  src.test.it.polito.ezshop.classes.UserObjTest::**testSetRoleNull**  |
### Code coverage report

    <Add here the screenshot report of the statement and branch coverage obtained using
    the Eclemma tool. >


### Loop coverage analysis

    <Identify significant loops in the units and reports the test cases
    developed to cover zero, one or multiple iterations >

|Unit name | Loop rows | Number of iterations | JUnit test case |
|---|---|---|---|
|||||
|||||
||||||



