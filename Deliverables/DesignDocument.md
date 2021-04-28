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

## Scenario 1.1
```plantuml
@startuml
actor ShopManager
participant "/ : Shop" as Shop
participant "/ : ProductOrderManager" as ProductOrderManager
participant "p : Product" as Product
participant "pos : Position" as Position

ShopManager -> Shop: 1 : createProductType(description, productCode, pricePerUnit, note)
activate Shop

Shop -> ProductOrderManager: 2 : createProductType()
activate ProductOrderManager
create Product
ProductOrderManager -> Product: 3 : new

deactivate ProductOrderManager
deactivate Shop
ShopManager -> Shop: 4 : updatePosition(id, position)
activate Shop
Shop -> ProductOrderManager: 5 : updatePosition(id, position)
activate ProductOrderManager
create Position
ProductOrderManager -> Position: 6 : new 

ProductOrderManager -> Product: 7 : setPosition(pos)
activate Product
deactivate Product
deactivate ProductOrderManager
deactivate Shop
@enduml
```

## Scenario 1.2
```plantuml
@startuml
@startuml
participant "/ : Shop" as Shop
participant "/ : ProductOrderManager" as ProductOrderManager
participant "p : Product" as Product
participant "pos : Position" as Position
activate Shop
Shop -> ProductOrderManager: 1 : getProductTypeByBarCode(barCode)
activate ProductOrderManager
deactivate ProductOrderManager


Shop -> ProductOrderManager: 2 : updatePosition(p.id, position)
activate ProductOrderManager
create Position
ProductOrderManager -> Position: 3 : new 

ProductOrderManager -> Product: 4 : setPosition(pos)
activate Product
deactivate Product
deactivate ProductOrderManager
deactivate Shop
@enduml
```

## Scenario 1.3
```plantuml
@startuml
participant "/ : Shop" as Shop
participant "/ : ProductOrderManager" as ProductOrderManager


activate Shop
Shop -> ProductOrderManager: 1 : getProductTypeByBarCode(barCode)
activate ProductOrderManager
deactivate ProductOrderManager


Shop -> ProductOrderManager: 2 : updateProduct(newPrice)
activate ProductOrderManager





deactivate ProductOrderManager
deactivate Shop
@enduml
```

## Scenario 2.1
```plantuml
@startuml
actor ShopManager
participant "/ : Shop" as Shop
participant "/ : UserManager" as UserManager
participant "u : User" as User
ShopManager -> Shop: 1: createUser()
activate Shop
Shop -> UserManager: 2: createUser()
activate UserManager
create User
UserManager -> User: 3: new
deactivate Shop
deactivate UserManager

ShopManager -> Shop: 4: getUser(id)
activate Shop
Shop -> UserManager: 5: getUser(id)
activate UserManager
deactivate UserManager
deactivate Shop

ShopManager -> Shop: 5: updateUserRights(u.id, role)
activate Shop
Shop -> UserManager: 6: updateUserRights(id, role)
activate UserManager
UserManager -> User: 7: setRole()
activate User
deactivate User

deactivate UserManager

@enduml
```
## Scenario 2.2
```plantuml
@startuml
actor ShopManager
participant "/ : Shop" as Shop
participant "/ : UserManager" as UserManager
participant "u : User" as User

ShopManager -> Shop: 4: getUser()
activate Shop
Shop -> UserManager
activate UserManager
deactivate UserManager
deactivate Shop

ShopManager -> Shop: 1: deleteUser(u.id)
activate Shop
Shop -> UserManager: 2: deleteUser(u.id)
activate UserManager
UserManager -> User: delete
destroy User
deactivate UserManager
deactivate Shop

@enduml
```
## Scenario 2.3
```plantuml
@startuml
actor ShopManager
participant "/ : Shop" as Shop
participant "/ : UserManager" as UserManager
participant "u : User" as User
ShopManager -> Shop: 1: updateUserRights(u.id, role)
activate Shop
Shop -> UserManager: 2: updateUserRights(id, role)
activate UserManager
UserManager -> User: 3: setRole()
activate User
deactivate User
deactivate UserManager
deactivate Shop

@enduml
```

## Scenarion 8.1
```plantuml
@startuml

participant "/ : Shop" as Shop
participant "/ : ProductOrderManager" as ProductOrderManager
participant "/ : TransactionManager" as TransactionManager
participant "/ : Transaction" as Transaction
Shop -> TransactionManager: startReturnTransacion()
activate TransactionManager
TransactionManager -> ProductOrderManager: updateQuantity()
activate ProductOrderManager
ProductOrderManager -> TransactionManager: return()
deactivate ProductOrderManager
TransactionManager -> TransactionManager:returnCreditCardPayment()
activate TransactionManager
TransactionManager -> TransactionManager: luhnAlgorithm()
note right: Card validated
deactivate TransactionManager 
TransactionManager  -> Shop: return()
deactivate TransactionManager 
Shop -> TransactionManager: endReturnTransaciton()
activate TransactionManager
TransactionManager-> Transaction: getAmount()
activate Transaction
Transaction -> TransactionManger: return()
deactivate Transaction
TransactionManager -> TransactionManager: recordBalance() 
TransactionManager -> Shop:return()
deactivate TransactionManager 
@enduml
```

## Scenarion 8.2

```plantuml
@startuml

participant "/ : Shop" as Shop
participant "/ : ProductOrderManager" as ProductOrderManager
participant "/ : TransactionManager" as TransactionManager
participant "/ : Transaction" as Transaction
Shop -> TransactionManager: startReturnTransacion()
activate TransactionManager
TransactionManager -> ProductOrderManager: updateQuantity()
activate ProductOrderManager
ProductOrderManager -> TransactionManager: return()
deactivate ProductOrderManager
TransactionManager -> TransactionManager:returnCashPayment()
TransactionManager  -> Shop: return()
deactivate TransactionManager 
Shop -> TransactionManager: endReturnTransaciton()
activate TransactionManager
TransactionManager-> Transaction: getAmount()
activate Transaction
Transaction -> TransactionManger: return()
deactivate Transaction
TransactionManager -> TransactionManager: recordBalance() 
TransactionManager -> Shop:return()
deactivate TransactionManager 
@enduml
```

## Scenarin 9.1
```plantuml
@startuml

participant "/ : Shop" as Shop
participant "/ : TransactionManager" as TransactionManager
Shop -> TransactionManager: getCreditsAndDebits()
activate TransactionManager
TransactionManager  -> Shop: return()
deactivate TransactionManager 
@enduml
```

## Scenario 10.1

```plantuml
@startuml


participant "/ : TransactionManager" as TransactionManager
participant "/ : Transaction" as Transaction
TransactionManager-> Transaction: getAmount()
activate Transaction
Transaction -> TransactionManger: return()
deactivate Transaction
TransactionManager -> TransactionManager: recordBalance() 
TransactionManager -> Shop:return()

@enduml
```

## Scenario 10.2

```plantuml
@startuml


participant "/ : TransactionManager" as TransactionManager
participant "/ : Transaction" as Transaction

TransactionManager-> Transaction: getAmount()
activate Transaction
Transaction -> TransactionManger: return()
deactivate Transaction
TransactionManager -> TransactionManager: recordBalance() 
TransactionManager -> Shop:return()
@enduml
```
