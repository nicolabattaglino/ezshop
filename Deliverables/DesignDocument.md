# Design Document 


Authors: 

Date:

Version:


# Contents

- [High level design](#package-diagram)
- [Low level design](#class-diagram)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)

# Instructions

The design must satisfy the Official Requirements document, notably functional and non functional requirements

# High level design 

<discuss architectural styles used, if any>
<report package diagram>






# Low level design

<for each package, report class diagram>


```plantuml

@startuml

interface EzShopInterface {

    +reset()
    +createUser(username: String, password: String, role: String) : Integer
    +deleteUser(id: Integer): boolean
    +getAllUser(): List<User>
    +getUser(id: Integer): User
    +updateUserRights(id: Integer, role: String): Boolean
    +getUser(id: Integer) : User

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
    +getAllCustomers(): List<Customer>
    +getCustomer(id : Integer): Customer
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

class Shop 

class UserManager{
    -userIdGen: Integer
    -loggedUser: User
    
    +createUser(username: String, password: String, role: String) : Integer
    +deleteUser(id: Integer) : Boolean
    +getAllUsers() : List<User>
    +updateUserRights(id: Integer, role: String) : Boolean
    +getUser(id: Integer) : User

    +login(username: String, password: String) : User
    +logout() : boolean

}

class User {
    -id: Integer
    -username: String
    -password: String
    -role: String
}

class Customer {
    -id: Integer
    -name: String
    -surname: String
}

LoyaltyCard "0..1" <- Customer: -loyaltyCard

class LoyaltyCard {
    -cardCode: String
    -points: Integer
}

UserManager -->"*" User: -userList
Customer "*"<-- CustomerManager: -customerMap
LoyaltyCard "*"<-- CustomerManager: -cardMap

class CustomerManager {
    -userIdGen: Integer
    -loyaltyCardIdGen: long

    +defineCustomer(customerName: String): Customer
    +modifyCustomer(id: Integer, newCustomerName: String, newCustomerCard): boolean
    +deleteCustomer(id: Integer): boolean
    +getAllCustomers(): List<Customer>
    +getCustomer(id : Integer): Customer
    +createCard(): String
    +attachCardToCustomer(customerCard: String, customerId: String ): boolean
    +modifyPointsOnCard(customerCard: String, pointsToBeAdded: Integer): boolean
}


class ProductOrderManager {

    -productIdGen: Integer
    -orderIdGen: Integer

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
}

ProductOrderManager -->"*" ProductType: -productMap
ProductOrderManager -->"*" Order: -orderMap

class TransactionManager {
    -transactionMap <transactionID, transaction>
    -balanceOrderedList <Balance>
    +startSaleTransaction() : Integer
    +addProductToSale(transactionId: Integer, productCode: String, amount: Integer): boolean
    +deleteProductFromSale(transactionId: Integer, productCode: String, amount: Integer): boolean
    +applyDiscountRateToProduct(transactionId: Integer, productCode: String, discountRate: double): boolean
    +applyDiscountRateToSale(transactionId: Integer, discountRate: double) : boolean
    +computePointsForSale(transactionId: Integer): int
    +endSaleTransaction(transactionId: Integer): boolean
    +getSaleTransaction(transactionId: Integer): boolean
    +startReturnTransaction(transactionId: Integer): Integer
    +returnProduct(returnId : Integer, productCode: String, amount: int): boolean
    +endReturnTransaction(returnId : Integer, commit: boolean): boolean
    +deleteReturnTransaction(returnId: Integer): boolean
    +receiveCashPayment(transactionId: Integer, cash: double): double
    +receiveCreditCardPayment(transactionId: Integer, creditCard: String): boolean
    +returnCashPayment(returnId: Integer): double
    +returnCreditCardPayment(returnId: Integer, creditCard: String): double
    -recordBalanceUpdate ( double toBeAdded) : boolean
    +getCreditsAndDebits(from: LocalDate, to: LocalDate): List<BalanceOperation>
    +computeBalance(): double
    -luhnAlgorithm (int creditCardNumber): boolean
}

class Transaction {
    -double amount
    -string creditCard
    -int ID
    
}
TransactionManager <-- Transaction : transactionID

class Balance{
    -int Amount
    -string From
    -string to
}

TransactionManager <-- Balance: OrderedList


Shop --> UserManager
Shop --> CustomerManager
Shop --> ProductOrderManager
Shop -->TransactionManager

EzShopInterface <|.. Shop

class Credit 
class Debit

Credit --|> FinancialTransaction
Debit --|> FinancialTransaction

class Order{
    -id: Integer
    -supplier
    -pricePerUnit: double
    -quantity: Integer
    -status: OrderStatus
}

enum OrderStatus{
    ISSUED
    ORDERED
    PAYED
    COMPLETED
}

OrderStatus <- Order

Order --|> Debit
ReturnTransaction --|> Debit

class ProductType {
    -id: Integer
    -String: barCode
    -String: description
    -double: sellPrice
    -int: discountRate
    -String: notes

}

class Product

ProductType <--"*" Product: -type 
class Position {
    -aisleID: Integer
    -rackID: Integer
    -levelID: Integer
}
ProductType ->"0..1" Position: -position


class SaleTransaction {
    ID 
    date
    time
    cost
    paymentType
    discount rate
    loyalityCardCode
    customerIds

    addProductToSale()
    deleteProductFromSale()
    applyDiscountRateToSale()
    applyDiscountRateToProduct() 
    computePointsForSale()
}

SaleTransaction -- "*" ProductType
(SaleTransaction,ProductType).. Quantity
SaleTransaction -|> Credit

class Quantity {
    quantity: Integer
}


class LoyaltyCard {
    ID
    points
    attachCustomer()
}



SaleTransaction "*" --> "0..1" LoyaltyCard: -loyaltyCard

Order "*" - ProductType

class ReturnTransaction {
  quantity
  returnedValue

endReturnTransaction()
}

ReturnTransaction "*" - SaleTransaction
ReturnTransaction "*" - ProductType
@enduml

```

```plantuml
@startuml

class UsersManager{
    +createUser(username: String, password: String, role: String) : Integer
    +deleteUser(id: Integer) : Boolean
    +getAllUsers() : List<User>
    +updateUserRights(id: Integer, role: String) : Boolean
    +getUser(id: Integer) : User

}

class CustomersManager {
  
    +defineCustomer(customerName: String) : Customer
    +modifyCustomer(id: Integer, newCustomerName: String, newCustomerCard) : boolean
    +deleteCustomer(id: Integer) : boolean
    +getAllCustomers() : List<Customer>
    +getCustomer(id : Integer): Customer
    +createCard() : String
    +attachCardToCustomer(customerCard: String, customerId: String ) : boolean
    +modifyPointsOnCard(customerCard: String, pointsToBeAdded: Integer) : boolean
}


class User {
    id: Integer
    username: String
    password: String
    role: String

}

class Administrator {
    
    
}

class Customer {
    id: Integer
    customerName: String
    surname: String
   
    
    
}

class Card {
    cardCode: String
    points: Integer
 
}

class Authentication {
    login(username: String, password: String) : User
    logout() : boolean
}

Administrator -- UsersManager
User <|-- "1" Administrator
CustomersManager -- "*" Customer
User "*" -- CustomersManager
CustomersManager -- "*" Card
User -- Authentication
@enduml

```

```plantuml
@startuml
'skinparam classAttributeIconSize 0
class ProductManager {
    +createProductType(description: String, productCode: String, pricePerUnit: double, note: String): Integer
    +updateProduct(id: Integer, newDescription: String, newCode: String, newPrice: double, newNote: String): boolean
    +deleteProductType(id: Integer): boolean
    +getAllProductTypes():  List<ProductType>
    +getProductTypeByBarCode(barCode: String): ProductType
    +getProductTypesByDescription(description: String): List<ProductType>
}
class OrderManager {
    +updateQuantity(Integer productId, int toBeAdded): boolean
    +updatePosition(Integer productId, String newPos): boolean
    +issueReorder(String productCode, int quantity, double pricePerUnit): Integer
    +payOrderFor(String productCode, int quantity, double pricePerUnit): Integer
    +payOrder(Integer orderId): boolean
    +recordOrderArrival(Integer orderId): boolean
    +getAllOrders(): List<Order> 
}

class Product{
}

class Order{
    -id: Integer
    -supplier
    -pricePerUnit: double
    -quantity: Integer
    -status: OrderStatus
}

enum OrderStatus{
    ISSUED
    ORDERED
    PAYED
    COMPLETED
}

OrderStatus <-- Order
ProductType <--"*" Product: -type
Order "*" --> ProductType: products

class ProductType {
    -/id: Integer
    -String: barCode
    -String: description
    -double: sellPrice
    -int: discountRate
    -String: notes

    +ProductType(description: String, productCode: String, pricePerUnit: double, note: String)
    +GetBarcode(): String
}

note left of ProductType::id
  {id = barCode.hashCode()}
end note

class Position {
    -aisleID: Integer
    -rackID: Integer
    -levelID: Integer
}

ProductManager -->"*" ProductType: -productMap
ProductType -->"0..1" Position: -position
@enduml
```


# Verification traceability matrix

\<for each functional requirement from the requirement document, list which classes concur to implement it>











# Verification sequence diagrams 
\<select key scenarios from the requirement document. For each of them define a sequence diagram showing that the scenario can be implemented by the classes and methods in the design>

Scenarion 8.1
```plantuml
@startuml
Shop -> transactionManager: startReturnTransacion()
activate TransactionManager
TransacrionManager -> ProductOrderManager: updateQuantity()
ProductOrderManager -> TransactionManager: return
TransactionManager -> TransactionManager:returnCreditCardPayment()
activate TransactionManager
TransactionManager -> TransactionManager: luhnAlgorithm()
note right: Card validated
deactivate TransactionManager 
TransactionManager  -> Shop: return
deactivate TransactionManager 
Shop -> TransactionManager: endReturnTransaciton()
activate TransactionManager
TransactionManager -> Shop:return 
deactivate TransactionManager 
@enduml
```

Scenarin 9.1
```plantuml
@startuml
Shop -> TransactionManager: getCreditsAndDebits()
activate TransactionManager
TransactionManager  -> : return
deactivate TransactionManager 
@enduml
```