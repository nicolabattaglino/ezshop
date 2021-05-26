# Integration and API Test Documentation

Authors:

Date:

Version:

# Contents

- [Dependency graph](#dependency graph)

- [Integration approach](#integration)

- [Tests](#tests)

- [Scenarios](#scenarios)

- [Coverage of scenarios and FR](#scenario-coverage)
- [Coverage of non-functional requirements](#nfr-coverage)



# Dependency graph 

     <report the here the dependency graph of the classes in EzShop, using plantuml>

```plantuml

@startuml
scale 0.8
interface EZShopInterface {
    +reset()
    +createUser(username: String, password: String, role: String) : Integer
    +deleteUser(id: Integer): boolean
    +getAllUsers(): List<User>
    +getUser(id: Integer): User
    +updateUserRights(id: Integer, role: String): Boolean


    +login(username: String, password: String): User
    +logout(): boolean

    +createProductType(description: String, productCode: String, pricePerUnit: double, note: String): Integer
    +updateProduct(id: Integer, newDescription: String, newCode: String, newPrice: double, newNote: String): boolean
    +deleteProductType(id: Integer): boolean
    +getAllProductTypes():  List<ProductType>
    +getProductTypeByBarCode(barCode: String): ProductType
    +getProductTypesByDescription(description: String): List<ProductType>

    +updateQuantity(Integer productId, int toBeAdded): boolean
    +updatePosition(Integer productId, String newPos): boolean
    +issueOrder(String productCode, int quantity, double pricePerUnit): Integer
    +payOrderFor(String productCode, int quantity, double pricePerUnit): Integer
    +payOrder(Integer orderId): boolean
    +recordOrderArrival(Integer orderId): boolean
    +getAllOrders(): List<Order> 

    +defineCustomer(customerName: String): Customer
    +modifyCustomer(id: Integer, newCustomerName: String, newCustomerCard): boolean
    +deleteCustomer(id: Integer): boolean
    +getCustomer(id : Integer): Customer
    +getAllCustomers(): List<Customer>

    +createCard(): String
    +attachCardToCustomer(customerCard: String, customerId: String ): boolean
    +modifyPointsOnCard(customerCard: String, pointsToBeAdded: Integer): boolean

    +startSaleTransaction() : Integer
    +addProductToSale(transactionId: Integer, productCode: String, amount: Integer): boolean
    +deleteProductFromSale(transactionId: Integer, productCode: String, amount: Integer): boolean
    +applyDiscountRateToProduct(transactionId: Integer, productCode: String, discountRate: double): boolean
    +applyDiscountRateToSale(transactionId: Integer, discountRate: double) : boolean
    +computePointsForSale(transactionId: Integer): int
    +endSaleTransaction(transactionId: Integer): boolean
    +deleteSaleTransaction(transactionId: Integer) : boolean
    +getSaleTransaction(transactionId: Integer): boolean
    +startReturnTransaction(transactionId: Integer): Integer
    +returnProduct(returnId : Integer, productCode: String, amount: int): boolean
    +endReturnTransaction(returnId : Integer, commit: boolean): boolean
    +deleteReturnTransaction(returnId: Integer): boolean
    +receiveCashPayment(transactionId: Integer, cash: double): double
    +receiveCreditCardPayment(transactionId: Integer, creditCard: String): boolean
    +returnCashPayment(returnId: Integer): double
    +returnCreditCardPayment(returnId: Integer, creditCard: String): double

    +recordBalanceUpdate(toBeAdded: double): boolean
    +getCreditsAndDebits(from: LocalDate, to: LocalDate): List<BalanceOperation>
    +computeBalance(): double
}



class Shop {
    +addOrder(order: Order): boolean
}

Shop.|>EZShopInterface

class UserManager{
    -userIdGen: Integer
    -loggedUser: User
    +{static}USERS_PATH: String
    +{static}USERS_ID_PATH: String
    
    +createUser(username: String, password: String, role: String) : Integer
    +deleteUser(id: Integer) : Boolean
    +getAllUsers() : List<User>
    +getUser(id: Integer) : User
    +updateUserRights(id: Integer, role: String) : Boolean
    +login(username: String, password: String) : User
    +logout() : boolean
    -persistUsers()
    -persistUsersId()
    +clear()

    +getLoggedUser() : User
}

class User {
    
    -id: Integer
    -username: String
    -password: String
}

enum UserRole{
    CASHIER
    SHOP_MANAGER
    ADMINISTRATOR
}

enum ReturnStatus{
    NEW
    CLOSED
    ENDED
}

UserRole <. User 

class Customer {
    -id: Integer
    -customerName: String
}



LoyaltyCard  <. Customer

class LoyaltyCard {
    -cardCode: String
    -points: Integer
    -isAttached: boolean
}

UserManager ..> User
Customer <.. CustomerManager
LoyaltyCard <.. CustomerManager

class CustomerManager {
    -customerIdGen: Integer
    -loyaltyCardIdGen: long
    +{static}CARD_PATH: String
    +{static}CARD_ID_PATH: String
    +{static}CUSTOMER_ID_PATH: String
    +{static}CUSTOMER_PATH: String
    
    +defineCustomer(customerName: String): Customer
    +modifyCustomer(id: Integer, newCustomerName: String, newCustomerCard): boolean
    +deleteCustomer(id: Integer): boolean
    +getCustomer(id : Integer): Customer
    +getAllCustomers(): List<Customer>
    +createCard(): String
    +attachCardToCustomer(customerCard: String, customerId: String ): boolean
    +modifyPointsOnCard(customerCard: String, pointsToBeAdded: Integer): boolean
    -persistCards()
    -persistCustomers()
    -persistCardsId()
    -persistCustomersId()
    +clear()
}


class ProductOrderManager {

    -productIdGen: int
    -orderIdGen: int
    +{static}PRODUCTS_PATH: String
    +{static}PRODUCT_GEN_PATH: String
    +{static}ORDER_GEN_PATH: String
    
    +checkBarcode(barCode: String): boolean

    +createProductType(description: String, productCode: String, pricePerUnit: double, note: String): Integer
    +updateProduct(id: Integer, newDescription: String, newCode: String, newPrice: double, newNote: String): boolean
    +deleteProductType(id: Integer): boolean
    +getAllProductTypes():  List<ProductType>
    +getProductTypeByBarCode(barCode: String): ProductType
    +getProductTypesByDescription(description: String): List<ProductType>

    +updateQuantity(Integer productId, int toBeAdded): boolean
    +updatePosition(Integer productId, String newPos): boolean

    +issueOrder(String productCode, int quantity, double pricePerUnit): Integer
    +payOrderFor(String productCode, int quantity, double pricePerUnit): Integer
    
    +payOrder(Integer orderId): boolean
    +recordOrderArrival(Integer orderId): boolean
    -persistGen()
    -persistProducts()
    +clear()

}

ProductOrderManager ..> ProductType

class TransactionManager {
    -saleGen:  int
    -returnGen:int 
    -balanceOperationGen:  int
    -{static}ORDER_PATH: String
    -{static}SALE_PATH: String
    -{static}RETURN_PATH: String
    -{static}CREDITCARD_PATH: String
    -{static}BALANCEOPERATION_PATH: String
    -{static}GENERATOR_PATH: String
    
    +startSaleTransaction() : Integer
    +addProductToSale(transactionId: Integer, productCode: String, amount: Integer): boolean
    +deleteProductFromSale(transactionId: Integer, productCode: String, amount: Integer): boolean
    +applyDiscountRateToProduct(transactionId: Integer, productCode: String, discountRate: double): boolean
    +applyDiscountRateToSale(transactionId: Integer, discountRate: double) : boolean
    +computePointsForSale(transactionId: Integer): int
    +endSaleTransaction(transactionId: Integer): boolean
    +deleteSaleTransaction(transactionId: Integer) : boolean
    +getSaleTransaction(transactionId: Integer): boolean
    +startReturnTransaction(transactionId: Integer): Integer
    +returnProduct(returnId : Integer, productCode: String, amount: int): boolean
    +endReturnTransaction(returnId : Integer, commit: boolean): boolean
    +deleteReturnTransaction(returnId: Integer): boolean
    +receiveCashPayment(transactionId: Integer, cash: double): double
    +receiveCreditCardPayment(transactionId: Integer, creditCard: String): boolean
    +returnCashPayment(returnId: Integer): double
    +returnCreditCardPayment(returnId: Integer, creditCard: String): double
    +recordBalanceUpdate ( double toBeAdded) : boolean
    +getCreditsAndDebits(from: LocalDate, to: LocalDate): List<BalanceOperation>
    +computeBalance(): double
    +getAllOrders(): List<Order>
    
    -persistOrders()
    -persistCards()
    -persistSales()
    -persistReturns()
    -persistBalanceOperations()
    -persistGenerators()
    +luhnAlgorithm (String creditCard): boolean
    +clear()
     
    +getReturnTransaction(transactionId: Integer): ReturnTransaction
    +addCompletedOrder(orderId: Integer): Order
    +addOrder(order: Order): boolean
}


TransactionManager ..> BalanceOperation
TransactionManager ..> CreditCard
TransactionManager ..> Order
TransactionManager ..> SaleTransaction

Shop ..> UserManager
Shop ..> CustomerManager
Shop ..> ProductOrderManager
Shop ..> TransactionManager

Shop <.. UserManager
Shop <.. CustomerManager
Shop <.. ProductOrderManager
Shop <.. TransactionManager

class CreditCard {
    -number: String
    -balance: double
}


class Credit 
class Debit

Credit --|> BalanceOperation
Debit --|> BalanceOperation

abstract BalanceOperation {
    
    -balanceId: int
    -type: String
    -money: double
    -date: LocalDate
    
}
class Order{
    -orderId: Integer
    -supplier: String
    -pricePerUnit: double
    -quantity: Integer
}

enum OrderStatus{
    ISSUED
    PAYED
    COMPLETED
}

Order .> OrderStatus

Order ..> Debit
ReturnTransaction --|> Debit

class ProductType {
    -id: Integer
    -barCode: String

    -description: String
    -sellPrice: double
    -discountRate: double
    -notes: String
    -amount: int
}


class Position {
    -aisleID: Integer
    -rackID: Integer
    -levelID: Integer
    -empty: boolean
}


ProductType .> Position


class SaleTransaction {
    
    -ticketNumber: Integer
    -paymentType : String
    -price: double
    -discountRate : double
   
    
    -updatePrice()
    +deleteEntry(entry: TicketEntry)
    +addEntry(entry: TicketEntry)
    +addProduct(p: ProductType, quantity : Integer) : boolean
    
}

enum SaleStatus {
    STARTED,
    CLOSED,
    PAYED
}

SaleTransaction  ..> TicketEntry
SaleTransaction  ..> SaleStatus 


SaleTransaction -|> Credit

class TicketEntry {
    -  barCode: String
    -  productDescription : String
    -  amount: int 
    -  pricePerUnit: double
    -  discountRate: double
}

SaleTransaction ..> LoyaltyCard

Order .> ProductType

class ReturnTransaction {
    -transactionId: int
    -price: double
}

ReturnTransaction ..> ReturnStatus
ReturnTransaction ..> TicketEntry


TransactionManager ..> ProductOrderManager
@enduml

```


# Integration approach

    <Write here the integration sequence you adopted, in general terms (top down, bottom up, mixed) and as sequence
    (ex: step1: class A, step 2: class A+B, step 3: class A+B+C, etc)> 
    <Some steps may  correspond to unit testing (ex step1 in ex above), presented in other document UnitTestReport.md>
    <One step will  correspond to API testing>



#  Tests

   <define below a table for each integration step. For each integration step report the group of classes under test, and the names of
     JUnit test cases applied to them> JUnit test classes should be here src/test/java/it/polito/ezshop

## Step 1
| Classes  | JUnit test cases |
|-----|----|
|   |   |


## Step 2
| Classes  | JUnit test cases |
|-------|------|
|   |   |


## Step n 

   

| Classes  | JUnit test cases |
|------|------|
|   |   |




# Scenarios


<If needed, define here additional scenarios for the application. Scenarios should be named
 referring the UC in the OfficialRequirements that they detail>

## Scenario UCx.y

| Scenario |  name |
| ------------- |:-------------:|
|  Precondition     |  |
|  Post condition     |   |
| Step#        | Description  |
|  1     |  ... |
|  2     |  ... |



# Coverage of Scenarios and FR


<Report in the following table the coverage of  scenarios (from official requirements and from above) vs FR. 
Report also for each of the scenarios the (one or more) API JUnit tests that cover it. >




| Scenario ID | Functional Requirements covered | JUnit  Test(s) |
| ----------- | ------------------------------- | ----------- |
| 1-1         | FR-3.1                          | src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testCreateProductTypeOk** <br/>src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testCreateProductTypeInvalidCode**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testCreateProductTypeInvalidDescription** |
| 1-1         | FR-4.2                         | src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testUpdatePositionInvalid**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testUpdatePositionReset**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testUpdatePositionOk** |
| 1-1         | FR-3.1, FR1.5                 | src.test.it.polito.ezshop.classes.EZShopTest::**testCreateProductType** |
| 1-1         | FR-4.2, FR1.5                  | src.test.it.polito.ezshop.classes.EZShopTest::**testUpdatePosition** |
| 1-2         | FR-3.4                          | src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testGetProductTypeByBarCodeInvalidCode**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testGetProductTypeByBarCodeOk** |
| 1-2         | FR-4.2                          | src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testUpdatePositionInvalid**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testUpdatePositionReset**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testUpdatePositionOk** |
| 1-2         | FR-3.4, FR1.5                   | src.test.it.polito.ezshop.classes.EZShopTest::**testGetProductTypeByBarCode** |
| 1-2         | FR-4.2, FR1.5                   | src.test.it.polito.ezshop.classes.EZShopTest::**testUpdatePosition** |
| 1-3 | FR-3.4                          | src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testGetProductTypeByBarCodeInvalidCode**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testGetProductTypeByBarCodeOk** |
| 1-3 | FR-3.1 | src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testUpdateProductInvalidID**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testUpdateProductPriceInvalidPrice**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testUpdateProductInvalidBarcode**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testUpdateProductPriceCodeAlreadyExist**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testUpdateProductIdNotPresent**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testUpdateProductOK** |
| 1-3 | FR-3.4, FR1.5 | src.test.it.polito.ezshop.classes.EZShopTest::**testGetProductTypeByBarCode** |
| 1-3 | FR-3.1, FR1.5 | src.test.it.polito.ezshop.classes.EZShopTest::**testUpdateProduct** |
| - | FR-3.2 | src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testDeleteProductTypeInvalidId**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testDeleteProductTypeNotFound**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testDeleteProductTypeOk** |
| - | FR-3.2, FR1.5 | src.test.it.polito.ezshop.classes.EZShopTest::**testDeleteProductType** |
| - | FR-3.3 | src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testGetAllProductTypesEmpty**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testGetAllProductTypes** |
| - | FR-3.3, FR1.5 | src.test.it.polito.ezshop.classes.EZShopTest::**testGetAllProductTypes** |
| -           | FR-3.4                          | src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testGetProductTypesByDescriptionOk**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testGetProductTypesByDescriptionEmpty** |
| -           | FR-3.4, FR1.5 | src.test.it.polito.ezshop.classes.EZShopTest::**testGetProductTypesByDescription** |
| 2-1         | FR1.1, FR1.5                    |  src.test.it.polito.ezshop.classes.EZShopTest::**testCreateUser** |
| 2-2         | FR1.2, FR1.5                    |   src.test.it.polito.ezshop.classes.EZShopTest::**testDeleteUser**          |
| 2-3         | FR1.1, FR1.5                    |   src.test.it.polito.ezshop.classes.EZShopTest::**testUpdateUserRights**           |
| -           | FR1.3, FR1.5                    |  src.test.it.polito.ezshop.classes.EZShopTest::**testGetAllUsers**          |
| -           | FR1.4, FR1.5                    |  src.test.it.polito.ezshop.classes.EZShopTest::**testGetUser**        |
| 3-1      | FR4.3 | src.test.it.polito.ezshop.classes.ProductOrderManagerOrderTest::**testIssueOrderInvalid**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerOrderTest::**testIssueOrderOk** |
| 3-1 | FR4.3, FR1.5                    | src.test.it.polito.ezshop.classes.EZShopTest::**testIssueOrder** |
| 3-2 | FR4.5 | src.test.it.polito.ezshop.classes.ProductOrderManagerOrderTest::**testPayOrderInvalid**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerOrderTest::**testPayOrderNotPresent**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerOrderTest::**testPayOrderNotEnoughBalance**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerOrderTest::**testPayOrderOk** |
| 3-2 | FR4.5, FR1.5 | src.test.it.polito.ezshop.classes.EZShopTest::**testPayOrderInvalid**<br />src.test.it.polito.ezshop.classes.EZShopTest::**testPayOrderAdmin**<br />src.test.it.polito.ezshop.classes.EZShopTest::**testPayOrderShopManager** |
| 3-2 | FR8.1 | src.test.it.polito.ezshop.classes.Transaction::**testBalanceUpdate** |
| 3-3 | FR4.6 | src.test.it.polito.ezshop.classes.ProductOrderManagerOrderTest::**testRecordOrderArrivalInvalidOrderId**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerOrderTest::**testRecordOrderArrivalInvalidProductLocation**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerOrderTest::**testRecordOrderArrivalNotFound**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerOrderTest::**testRecordOrderArrivalOk** |
| 3-3 | FR4.6, FR1.5 | src.test.it.polito.ezshop.classes.EZShopTest::**recordOrderArrivalInvalid**<br />src.test.it.polito.ezshop.classes.EZShopTest::**recordOrderAdmin**<br />src.test.it.polito.ezshop.classes.EZShopTest::**recordOrderShopManager** |
| 3-3         | FR4.1                           | src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testUpdateQuantityInvalid**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testUpdateQuantityInvalid** |
| -           | FR4.4                           | src.test.it.polito.ezshop.classes.ProductOrderManagerOrderTest::**testPayOrderForInvalid**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerOrderTest::**testPayOrderForNoProduct**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerOrderTest::**testPayOrderForNotEnoughBalance**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerOrderTest::**testPayOrderForOk** |
| -           | FR4.4, FR1.5 | src.test.it.polito.ezshop.classes.EZShopTest::**testPayOrderForInvalid**<br />src.test.it.polito.ezshop.classes.EZShopTest::**testPayOrderForAdmin**<br />src.test.it.polito.ezshop.classes.EZShopTest::**testPayOrderForShopManager** |
| - | FR4.7 | src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testGetAllOrders** |
| - | FR4.7, FR1.5 | src.test.it.polito.ezshop.classes.EZShopTest::**getAllOrdersInvalid**<br />src.test.it.polito.ezshop.classes.EZShopTest::**testGetAllOrdersOk** |
| 4-1         | FR5.1, FR1.5                             | src.test.it.polito.ezshop.classes.EZShopTest::**testDefineCustomer**          |
| 4-2         | FR5.6, FR1.5                             |  src.test.it.polito.ezshop.classes.EZShopTest::**testAttachCardToCustomer**       |
| 4-2 | FR5.5, FR1.5                             |src.test.it.polito.ezshop.classes.EZShopTest::**testCreateCard** |
| 4-3         | FR5.1, FR1.5                             |   src.test.it.polito.ezshop.classes.EZShopTest::**testModifyCustomer**        |
| 4-4         | FR5.1, FR1.5                             |    src.test.it.polito.ezshop.classes.EZShopTest::**testModifyCustomer**       |
|  -          | FR5.2, FR1.5                             |    src.test.it.polito.ezshop.classes.EZShopTest::**testDeleteCustomer**      |
|  -          | FR5.3, FR1.5                             |    src.test.it.polito.ezshop.classes.EZShopTest::**testGetCustomer**      |
|  -          | FR5.4, FR1.5                             |  src.test.it.polito.ezshop.classes.EZShopTest::**testGetAllCustomers**     |
|  -          | FR5.7, FR1.5                             |    src.test.it.polito.ezshop.classes.EZShopTest::**testModifyPointsOnCard**   |
| 5-1         | FR1.5                           |  src.test.it.polito.ezshop.classes.EZShopTest::**testLogin**        |
| 5-2         | FR1.5                           |  src.test.it.polito.ezshop.classes.EZShopTest::**testLogout**      |
| 6-1         |                                 |   src.test.java.it.polito.ezshop.classes.TransactionManagerTest::**testStartSaleTransaction**          |
| 6-1            | FR1.5                           |    src.test.java.it.polito.ezshop.classes.EZShopTest::**testStartSaleTransaction**                                                          |
|6-1             |                                 |  src.test.java.it.polito.ezshop.classes.TransactionManagerTest::**testAddProductToSale**            |
|6-1             | FR1.5                           |  src.test.java.it.polito.ezshop.classes.EZShopTest::**testAddProductToSale**            |
|6-1             |                                 |  src.test.java.it.polito.ezshop.classes.TransactionManagerTest::**testGetSaleTransaction**            |
|6-1         | FR-3.4                          | src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testGetProductTypeByBarCodeInvalidCode**|
|6-1         | FR4.1                           | src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testUpdateQuantityInvalid**<br />src.test.it.polito.ezshop.classes.ProductOrderManagerTest::**testUpdateQuantityInvalid** |
|6-1             |                                 |  src.test.java.it.polito.ezshop.classes.TransactionManagerTest::**testEndSaleTransaction**            |
|6-1             |FR1.5                                 |  src.test.java.it.polito.ezshop.classes.EZShopTest::**testEndSaleTransaction**            |
|6-1             |                                 |  src.test.java.it.polito.ezshop.classes.TransactionManagerTest::**testEndSaleTransaction**            |
|6-1             |                                 |  src.test.java.it.polito.ezshop.classes.TransactionManagerTest::**testReceiveCreditCardPayment**            |




# Coverage of Non Functional Requirements


<Report in the following table the coverage of the Non Functional Requirements of the application - only those that can be tested with automated testing frameworks.>


### 

| Non Functional Requirement | Test name |
| -------------------------- | --------- |
|                            |           |
|                            |           |
|                            |           |
|                            |           |
|   NFR6                     |  src.test.it.polito.ezshop.classes.UserManager::**testCheckCardDigits**         |



