# TradeApp

A professional **Java console application** for managing trades in an **Energy Trading Database** using **SQL Server** and **JDBC**.

## Features

* Connects to SQL Server using JDBC
* Add new trades
* View all trades
* Update existing trades
* Delete trades
* Search trades by counterparty or commodity
* Menu-driven console interface

---

## Requirements

* Java **JDK 11+**
* Microsoft SQL Server (local or remote)
* Microsoft JDBC Driver for SQL Server ([download link](https://go.microsoft.com/fwlink/?linkid=2284500))

---

## Database Setup

1. Open **SQL Server Management Studio (SSMS)**.
2. Create a database:

   ```sql
   CREATE DATABASE EnergyTradingDB;
   ```
3. Switch to the database:

   ```sql
   USE EnergyTradingDB;
   ```
4. Create the `Trades` table:

   ```sql
   CREATE TABLE Trades (
   TradeID INT PRIMARY KEY IDENTITY(1,1),
   TradeDate DATE NOT NULL,
   Counterparty VARCHAR(100) NOT NULL,
   Commodity VARCHAR(50) NOT NULL, 
   Volume DECIMAL(10,2) NOT NULL, 
   Price DECIMAL(10,2) NOT NULL, 
   TradeType VARCHAR(10) CHECK (TradeType IN ('BUY','SELL'))
   );
   ```

---

## Configuration

Update the following in `TradeApp.java`:

```java
static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=EnergyTradingDB;encrypt=false;integratedSecurity=true;";

// For SQL Authentication:
static final String USER = "your_username";
static final String PASS = "your_password";
```

* Use `integratedSecurity=true` for Windows Authentication.
* Use `USER` and `PASS` for SQL Authentication.

---

## Running the Application

1. Compile:

   ```bash
   javac -cp ".;path/to/mssql-jdbc-12.x.x.jre11.jar" TradeApp.java
   ```
2. Run:

   ```bash
   java -cp ".;path/to/mssql-jdbc-12.x.x.jre11.jar" TradeApp
   ```

---

## Example Menu

```
--- Trade Management Menu ---
1. Add a Trade
2. View All Trades
3. Update Trade
4. Delete Trade
5. Search Trades by Counterparty/Commodity
6. Exit
```

---
![image alt](https://github.com/Chandan-Ubale/EnergyTradingDB/blob/master/result%201.png)
![image alt](https://github.com/Chandan-Ubale/EnergyTradingDB/blob/master/result%202.png)
![image alt](https://github.com/Chandan-Ubale/EnergyTradingDB/blob/master/result%203.png)
![image alt](https://github.com/Chandan-Ubale/EnergyTradingDB/blob/master/result%204.png)
## Notes

* Ensure SQL Server is running on **port 1433**.
* Add the JDBC `.jar` file to your **classpath**.
* For connection issues, verify authentication mode and firewall settings.

---

## License

This project is provided for educational purposes. Modify and extend as needed for production use.
