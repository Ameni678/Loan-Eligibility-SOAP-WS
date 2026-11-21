\# Loan Eligibility SOAP Web Service



\## Project Description



This project is a loan eligibility management system developed using \*\*Spring Boot\*\* and \*\*SOAP Web Services\*\*.



It allows users to evaluate loan applications, simulate loan repayments, consult application history and view statistics through a web interface.



\## Features



\- \*\*Loan Eligibility:\*\* Evaluate customer eligibility based on age, annual income, CIBIL score and employment status.

\- \*\*Risk Assessment:\*\* Determine the customer's risk level.

\- \*\*Loan Simulation:\*\* Calculate monthly payments, total interest and generate an amortization schedule.

\- \*\*Application History:\*\* Retrieve previous loan applications.

\- \*\*Statistics Dashboard:\*\* Display approved and rejected applications with graphical statistics.

\- \*\*Web Interface:\*\* Interactive interface developed with Thymeleaf.

\- \*\*SOAP API:\*\* Expose services through a WSDL contract.

\- \*\*Unit Testing:\*\* Validate business logic using JUnit 5.



\## Technologies Used



\- Java

\- Spring Boot 3.5.7

\- Spring Web Services (SOAP)

\- Maven

\- Thymeleaf

\- HTML / CSS / JavaScript

\- Chart.js

\- JUnit 5

\- SoapUI

\- NetBeans



\## Application URLs



| Endpoint | Description |

|---|---|

| `http://localhost:8081/` | Web interface |

| `http://localhost:8081/ws/loanEligibility.wsdl` | SOAP WSDL |

| `http://localhost:8081/ws` | SOAP endpoint |



\## SOAP Operations



\- Customer

\- LoanSimulation

\- LoanHistory

\- LoanStats



\## Testing



Run unit tests using:



```bash

mvn test

```



The project includes three JUnit 5 unit tests covering eligibility, multiple rejection criteria and invalid data.



\## Running the Project



1\. Clone the repository.

2\. Open the project in NetBeans or another Java IDE.

3\. Run the Spring Boot application.

4\. Access the web interface 

