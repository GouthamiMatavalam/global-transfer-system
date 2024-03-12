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

**Database Design :**
Designed Database with three tables as part of requirement. 
![image](https://github.com/GouthamiMatavalam/global-transfer-system/assets/38003356/3b7f4903-3874-4589-b014-97729eb1df75)

**Database Setup :**
I have used Docker to setup Postgres below are the commands used to do the setup. Believed docker is already available.
1. pull latest docker image - docker pull postgres 
2. to execute - docker run --name postgresdb -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=password -e POSTGRES_DB=GTSDB -d postgres
3. With above steps, you should be able to connect with any tool or can use psql. If you are getting exception "FATAL: password authentication failed for user "postgres" ". Please follow next step.
4. Edit " /usr/local/var/postgres/pg_hba.conf " file. From "local all all trust" to "local all all scram-sha-256". This should resolve the issue.

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




