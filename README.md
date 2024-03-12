# Global Transfer System

The Application is creted to perform below Functionalities.
1. Create an API - to list Account details based on ClientId
2. Create an API - to list Transactions for an account number, in descending order.
3. Create an API - transfer funds between accounts

## Built with
Java 17, Spring Boot, PostgresDB, Hiberante, Liquibase for schema migration, Gradle, Docker

**Prerequisites**
Below are the prerequisites required to run the application.

1. any IDE.
2. Java 17
3. Postman
4. Docker for DB setup

**Assumptions Considered while creating the Application:**

**Database** 
- Before deploying the application, beleived that the DB setup is done and a "GTSDB" is available.
- Defined schema creation in liquibase scripts, default schema name is "gtsdevschema". Can be changed in application.properties file - "db-schema" property.
- Initial DB setup is done with sample data using Liquibase.
- Amount field is considered as NUMERIC to support decimal precision.
- 
**Application**
- Account transactions are retreived based on, transaction date descending order.
- Account Trasfer :
    - Input consists of "fromAccountId", "toAccountId" and "Amount".
    - Assumed that Amount will be in String with amount followed by currency. eg : 100 EUR. 
    - Account Transfer is initiated when source account has enough funds to process.
    - Transfer amount currency should match the destination account currency.
    - A new record is created in Transactions Table, to hold successful and failed transactions. Remarks field is used to keep track of reason for failure, for Success it will be "Successfu Transaction".
    - Once transaction is successful, transferred amount is deducted from source account.
    - Currency conversion is done using - http://api.exchangerate.host/convert?access_key={accessKey}&from=USD&to=INR&amount=10
    - Personal AccessKey is generated after logging in to - https://exchangerate.host/login, and used in application.

**Database Design :**
Designed Database with three tables as part of requirement. 
![image](https://github.com/GouthamiMatavalam/global-transfer-system/assets/38003356/3b7f4903-3874-4589-b014-97729eb1df75)

**Database Setup :**
I have used Docker to setup Postgres below are the commands used to do the setup. Believed docker is already available.
1. pull latest docker image - docker pull postgres 
2. to execute - docker run --name postgresdb -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=password -e POSTGRES_DB=GTSDB -d postgres
3. With above steps, you should be able to connect with any tool or can use psql. If you are getting exception "FATAL: password authentication failed for user "postgres" ". Please follow next step.
4. Edit " /usr/local/var/postgres/pg_hba.conf " file. From "local all all trust" to "local all all scram-sha-256". This should resolve the issue.

**Application Setup :**
1. Clone the code using git clone and repo link.
2. Once the DB setup is done, If the DB connection properties are different from above. Please update them in application.properties file.
3. Add accessKey in application.properties file field "exchange.rate.key".
4. Application will run on localhost:8080 port. It will show actuator endpoints and also circuit breaker endpoints in "http://localhost:8080/actuator/"
5. The above changes are sufficient to run the application.

**Testing :**

_Get Accounts based on client Id : One client Id can have multiple accounts, with different currency._
![image](https://github.com/GouthamiMatavalam/global-transfer-system/assets/38003356/8b5fa8b2-c550-4be5-ac96-3f761ea26651)

_Get Account transactions based on Account Id : Transactions are to be displayed in descending order_
![image](https://github.com/GouthamiMatavalam/global-transfer-system/assets/38003356/9dfe5871-29c1-4454-8921-1df3c90aa9a9)

_Transfer amount from one Account to another Account : Transfer amount and deduct amount transferred in source account._

Amount Transferred and added to Destination Account

![image](https://github.com/GouthamiMatavalam/global-transfer-system/assets/38003356/f2746abb-4d26-452f-a951-b9e44cd19840)

Amount deducted from source account

![image](https://github.com/GouthamiMatavalam/global-transfer-system/assets/38003356/51bf2521-31f7-426c-9ac0-0b3c5ca1a374)











