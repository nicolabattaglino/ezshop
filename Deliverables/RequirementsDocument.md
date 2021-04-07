# Requirements Document 

Authors:

Date:

Version:

# Contents

- [Essential description](#essential-description)
- [Stakeholders](#stakeholders)
- [Context Diagram and interfaces](#context-diagram-and-interfaces)
	+ [Context Diagram](#context-diagram)
	+ [Interfaces](#interfaces) 
	
- [Stories and personas](#stories-and-personas)
- [Functional and non functional requirements](#functional-and-non-functional-requirements)
	+ [Functional Requirements](#functional-requirements)
	+ [Non functional requirements](#non-functional-requirements)
- [Use case diagram and use cases](#use-case-diagram-and-use-cases)
	+ [Use case diagram](#use-case-diagram)
	+ [Use cases](#use-cases)
    	+ [Relevant scenarios](#relevant-scenarios)
- [Glossary](#glossary)
- [System design](#system-design)
- [Deployment diagram](#deployment-diagram)

# Essential description

Small shops require a simple application to support the owner or manager. A small shop (ex a food shop) occupies 50-200 square meters, sells 500-2000 different item types, has one or a few cash registers 
EZShop is a software application to:
* manage sales
* manage inventory
* manage customers
* support accounting



# Stakeholders


| Stakeholder name  | Description | 
| ----------------- |:-----------:|
|   Stakeholder x..     |             | 

# Context Diagram and interfaces

## Context Diagram
\<Define here Context diagram using UML use case diagram>
```plantuml
@startuml


title Context Diagram


actor Owner

actor Product
actor Subscriber
actor Employee
actor :Card printer: as cardPrinter
actor :Inventory and Catalogue System: as IandCS
rectangle "EZShop System" as EZShopSystem{
(EZShop)
}
Subscriber --> EZShop
Owner -|> Subscriber
Employee -|>Subscriber
Product --> EZShop
EZShop <-- cardPrinter
IandCS -> EZShop


@enduml
```

\<actors are a subset of stakeholders>

## Interfaces
\<describe here each interface in the context diagram>

\<GUIs will be described graphically in a separate document>

| Actor | Logical Interface | Physical Interface  |
| ------------- |:-------------:| -----:|
|   Actor x..     |  |  |

# Stories and personas
\<A Persona is a realistic impersonation of an actor. Define here a few personas and describe in plain text how a persona interacts with the system>

\<Persona is-an-instance-of actor>

\<stories will be formalized later as scenarios in use cases>


# Functional and non functional requirements

## Functional Requirements

\<In the form DO SOMETHING, or VERB NOUN, describe high level capabilities of the system>

\<they match to high level use cases>

| ID        | Description  |
| ------------- |:-------------:| 
|  FR1     |  |
|  FR2     |   |
| FRx..  | | 

## Non Functional Requirements

\<Describe constraints on functional requirements>

| ID        | Type (efficiency, reliability, ..)           | Description  | Refers to |
| ------------- |:-------------:| :-----:| -----:|
|  NFR1     |   |  | |
|  NFR2     | |  | |
|  NFR3     | | | |
| NFRx .. | | | | 


# Use case diagram and use cases


## Use case diagram
\<define here UML Use case diagram UCD summarizing all use cases, and their relationships>


\<next describe here each use case in the UCD>
### Use case 1, UC1
| Actors Involved        |  |
| ------------- |:-------------:| 
|  Precondition     | \<Boolean expression, must evaluate to true before the UC can start> |  
|  Post condition     | \<Boolean expression, must evaluate to true after UC is finished> |
|  Nominal Scenario     | \<Textual description of actions executed by the UC> |
|  Variants     | \<other executions, ex in case of errors> |

##### Scenario 1.1 

\<describe here scenarios instances of UC1>

\<a scenario is a sequence of steps that corresponds to a particular execution of one use case>

\<a scenario is a more formal description of a story>

\<only relevant scenarios should be described>

| Scenario 1.1 | |
| ------------- |:-------------:| 
|  Precondition     | \<Boolean expression, must evaluate to true before the scenario can start> |
|  Post condition     | \<Boolean expression, must evaluate to true after scenario is finished> |
| Step#        | Description  |
|  1     |  |  
|  2     |  |
|  ...     |  |

##### Scenario 1.2

##### Scenario 1.x

### Use case 2, UC2
..

### Use case x, UCx
..



# Glossary

\<use UML class diagram to define important terms, or concepts in the domain of the system, and their relationships> 
```plantuml
@startuml

title Glossary


class "Subscriber" as subscriber{
Name
Surname
SSN
Address
Date of birth
Phone number 
Email
Password
}
class "Owner" as owner{
}
class "Employee" as employee{
Salary
}
class "Local Terminal" as LT{
}

class "EZ Shop" as EZShop{
}
class "Right" as right{
Right level
}
class "Fidelity Card" as fidelityCard{
ID
Fidelity points
Creation date 
Lost 
}
class "Inventory Management System?" as IMS{
}
class "Inventory Database" as Idb{
?
}
class "Product type" as PT{
Name
Barcode
Price 
Amount
}
class "Coupon" as coupon{
ID
Discount amount
Expiration date 
Used
}

class "Transaction" as transaction{
Amount
ID
Date
}
class "Expense" as expense {
}

class "Sale" as sale{
}

class "Amount" as amount{
Quantity
}
class "Product" as product{
Discount
}





note right of coupon : 10 points correspond to a coupon 
note right of fidelityCard :10€ correspond to a fidelity point

subscriber<|-- owner
subscriber<|-- employee
fidelityCard"1...*" -- subscriber
EZShop--"*"fidelityCard 
subscriber -- right
EZShop - LT
IMS - EZShop  
IMS -- Idb
EZShop -- "*"PT
PT -- "*"product

EZShop -- "*"transaction

transaction <|-- sale
transaction <|-- expense
fidelityCard -- "*"coupon 
sale - "0...1"coupon
sale"*" -- subscriber
(product,transaction). amount

@enduml
```

\<concepts are used consistently all over the document, ex in use cases, requirements etc>

# System Design
\<describe here system design>
```plantuml
@startuml

title System Design
class "EZ Shop" as EZShop{
F1 
F2
F3
F4
}

class "Local terminal" as localTerminal{
}

class "Software" as software 
class "Card reader" as printer
EZShop o-- localTerminal
EZShop o-- printer
localTerminal -- software



@enduml
```
\<must be consistent with Context diagram>

# Deployment Diagram 

\<describe here deployment diagram >

