# Banking API
The Banking API will manage the bank accounts of its users. It will be managed by the Bank's employees and admins. Employees and Admins count as Standard users with additional abilities.

# Features
* Employees can view all customer information, but not modify in any way.
* Admins can both view all user information, as well as directly modify it.
* Standard Users can register and login to see their account information.They can have either Checking or Savings accounts.
* All Users can update their personal information, such as username, password, first and last names, as well as email.
* User Accounts support withdrawals, deposits, and transfers.
* Transfer of funds are allowed between accounts owned by the same user, as well as between accounts owned by different users.


# To-do list:
* Password Hashing
* Paging and Sorting endpoints
* Use JSON Web Tokens (JWTs) instead of Session Storage
* Support DELETE requests for Users and Accounts
* Simulate Savings account interest rates.

# Technologies Used

- Java 8
- Maven
- Servlets
- PostgreSQL
- JDBC
- Postman
- Apache Tomcat 9
- AWS RDS

# Getting Started

1. Be sure to have Apache Tomcat 9 installed.
2. Be sure to have the Java 8 runtime environment installed.

- git clone https://github.com/210419-Appian/P1-BankingAPI-KevinV.git

# Contributors

- Kevin Varughese

