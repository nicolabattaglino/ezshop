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
|Positive|Correct|No|No|No|valid|luhn("79927398713")-> true| testLuhn|
|Negative|*|*|*|*|invalid|luhn("-79927398713")|testLuhn|
|*|Incorrect|*|*|*|Invalid|luhn("5")|testLuhn|
|*|*|Yes|*|*|Invalid|luhn("iduhsidh")|testLuhn|
|*|*|*|Yes|*|Invalid|luhn("")|testLuhn|
|*|*|*|*|Yes|Invalid|luhn(null)|testLuhn|

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

| Validity of the string | Presence of non-numerical values | Length of the String parameter | Validity of the String according to the check-digit | Description of the test case | JUnit test case |
|------------------------|----------------------------------|--------------------------------|-----------------------------------------------------|------------------------------|-----------------|
| null                   | *                                | *                              | *                                                   | T1(null; false)              |                 |
| yes                    | yes                              | *                              | *                                                   | T2("a2bcde!"; false)         |                 |
| yes                    | no                               | < 12                           | *                                                   | T3("1234567"; false)         |                 |
| yes                    | no                               | > 14                           | *                                                   |                              |                 |
| yes                    | no                               | [ 12,14 ]                      | no                                                  |                              |                 |
| yes                    | no                               | [ 12,14 ]                      | yes                                                 |                              |                 |

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
|Valid|Valid|T1("ADMINISTRATOR") -> role set|testSetRole|
|Valid|Valid|T2("CASHIER") -> role set|testSetRole|
|Valid|Valid|T3("SHOPMANAGER") -> role set|testSetRole|
|Invalid|Invalid|T4("t") -> role not set|testSetRole|
|NULL|Invalid|T5(null) -> role not set|testSetRole|



# White Box Unit Tests

### Test cases definition
    
    <JUnit test classes must be in src/test/java/it/polito/ezshop>
    <Report here all the created JUnit test cases, and the units/classes under test >
    <For traceability write the class and method name that contains the test case>


| Unit name | JUnit test case |
|--|--|
|||
|||
||||

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



