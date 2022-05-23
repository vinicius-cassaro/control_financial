# control_financial
Spring REST API for financial management, developed with Java 11, JWT for authentication, JUnit for the tests, Oracle and Redis Database.

## Requirements
### Functional Requirements
**FR01 -** The system must provide the system status query.\
**FR02 -** The system should allow user registration.\
**FR03 -** The system must allow user login.\
**FR04 -** The system should allow the user to update their registration.\
**FR05 -** The system should allow the user to disable his registration.\
**FR06 -** The system should allow the user to register a tag.\
**FR07 -** The system should allow the user to change their labels.\
**FR08 -** The system should allow the user to consult their labels.\
**FR09 -** The system should allow the user to delete their labels.\
**FR10 -** The system should allow the user to register a contract, assigning or not labels to it.\
**FR11 -** The system should automatically create all contract installments.\
**FR12 -** The system should automatically create an installment at each repetition of the interval for the cash flow with indeterminate installments.\
**FR13 -** The system should allow the user to update their contracts.\
**FR14 -** The system must automatically recreate all unpaid installments of the contract if the change changes the number of installments, the amount or the interval between installments.\
**FR15 -** The system should allow the user to consult their contracts.\
**FR16 -** The system should allow the user to consult their contract installments.\
**FR17 -** The system should allow the user to delete their contracts.\
**FR18 -** The system should allow the user to close contracts with indefinite installments.\
**FR19 -** The system should allow the user to receive/pay off installments.\
**FR20 -** The system should automatically receive installments of positive contracts after expiration.\
**FR21 -** The system must mark installments of negative contracts with an alert icon after expiration.\
**FR22 -** The system must add the addition to the value of the installments of the contracts after the expiration date.\
**FR23 -** The system must use the data to provide information such as balance and statistics.
  
### Non-functional Requirements
**NFR01 -** The system will be developed using the Spring ecosystem.\
**NFR02 -** The system's back-end will be developed with the Java language.\
**NFR03 -** The architectural pattern used will be MVC.\
**NFR04 -** The system will use JWT to perform the authentication.\
**NFR05 -** The Junit framework will be used for unit tests.\
**NFR06 -** The system will store the general data in an Oracle database.\
**NFR07 -** The system will store the logout token in an Oracle database.
</br></br>

## Use cases
### Diagram
<did>
<img src="https://github.com/vinicius-cassaro/control_financial/blob/main/documentation/images/Use%20Case%20Diagram%20-%20English.png">
</div>
</br></br>

## Architectural Documentation (C4 except the code)
### Context
<did>
<img src="https://github.com/vinicius-cassaro/control_financial/blob/main/documentation/images/System%20Context%20Diagram.png">
</div>
</br>

### Containers
<did>
<img src="https://github.com/vinicius-cassaro/control_financial/blob/main/documentation/images/Container%20Diagram.png">
</div>
</br>

### Components
<did>
<img src="https://github.com/vinicius-cassaro/control_financial/blob/main/documentation/images/Component%20Diagram.png">
</div>
</br></br>

## Database Documentation
### Entity Relationship (Conceitual)
<did>
<img src="https://github.com/vinicius-cassaro/control_financial/blob/main/documentation/images/Entity%20Relationship%20Diagram%20(Conceitual)%20-%20English.png">
</div>
</br>

### Entity Relationship (Logical)
<did>
<img src="https://github.com/vinicius-cassaro/control_financial/blob/main/documentation/images/Entity%20Relationship%20Diagram%20(Logical).png">
</div>
