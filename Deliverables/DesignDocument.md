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
class Shop{
    accountBalance
    productTypes
    saleTransactions

    startSaleTransaction()
    closeSaleTransaction()
    searchBarCodeProduct()
    getTicketByNumber()
    closeSaleTransaction()
    startReturnTransaction()
    returnProduct()
    deleteReturnTransaction()
    deleteSaleTicket()
	computeSalePoints()
    getSaleTicket()
    receiveCashPayment()
    receiveCreditCardPayment()
    returnPaymentCash()
    returnPaymentWithCreditCard()
}

class Credit 
class Debit

Credit --|> FinancialTransaction
Debit --|> FinancialTransaction

class Order
class Sale
class Return

Order --|> Debit
Sale --|> Credit
Return --|> Debit

class ProductType{
    barcode
    description
    sellPrice
    quantity
    discountRate
    notes
}

Shop - "*" ProductType

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

Shop --"*" SaleTransaction
SaleTransaction - "*" ProductType

class SaleTicket{
    ticketNumber
    printSaleTicket()
}

SaleTransaction -- SaleTicket

class LoyaltyCard {
    ID
    points
    attachCustomer()
}

class Customer {
    name
    surname
}

LoyaltyCard "0..1" - Customer

SaleTransaction "*" -- "0..1" LoyaltyCard

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
    User: List
    createUser(name: String, surname: String, password: String, privilege: String)
    modifyUser(u: User)
    deleteUser(u: User)
    listUsers()
    searchUser(name: String, surname: String)
    setRightsUser(u: User, privilege: String )
}

class CustomersManager {
    Customer: List
    createCustomer(name: String, surname: String) : Customer
    deleteCustomer(cu: Customer)
    listCustomers()
    createCard() : Card
    attachCard(cu: Customer, c: Card )
    modifyPoints(c: Card, points: Integer)
}


class User {
  name: String
  surname: String
  password: String
  privilege: String
  


}

class Administrator {
    
    
}

class Customer {
    name: String
    surname: String
    card: Card
   
    
    
}

class Card {
    id: Integer
    points: Integer
 
}

Administrator -- UsersManager
User <|-- "1" Administrator
CustomersManager -- "*" Customer
User "*" -- CustomersManager
CustomersManager -- "*" Card
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
    +Product(type: ProductType)
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

