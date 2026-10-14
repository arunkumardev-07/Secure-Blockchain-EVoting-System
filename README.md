# Secure Blockchain-Based E-Voting System

A full-stack academic project demonstrating electronic voting using Java, Spring Boot, MySQL, Solidity smart contracts, and a web-based frontend.

## Technologies Used

- **Backend:** Java 21, Spring Boot, Maven
- **Database:** MySQL 8
- **Blockchain:** Solidity, Hardhat, local Ethereum-compatible development network
- **Frontend:** HTML, CSS, JavaScript
- **Security:** BCrypt password hashing and SHA-256 hashing utilities
- **API:** REST APIs

## Key Features

- Voter registration and login
- Candidate and election management
- Java REST API backend
- Smart-contract-based vote recording
- Duplicate-vote prevention in the application
- Blockchain-based vote counting and results
- Web interface for interacting with the voting system

## Project Structure

```text
SecureEvotingSystem/
├── backend/
│   └── evoting-backend/
│       └── evoting-backend/
├── blockchain/
│   ├── contracts/
│   ├── scripts/
│   └── test/
├── frontend/
│   ├── assets/
│   ├── index.html
│   ├── results.html
│   ├── script.js
│   └── style.css
├── .gitignore
└── README.md
```

## Project Purpose

The goal of this project is to explore how a Java backend, relational database, web interface, and blockchain smart contract can work together in an electronic voting application.

It also provides practical experience with REST API development, database integration, smart contract development, and blockchain interaction.

## Academic Demonstration Only

This project is intended for learning and demonstration. It has not been independently audited and is **not suitable for real-world elections**. Blockchain integration alone does not guarantee voter anonymity, election integrity, or production-grade security.

## Author

**Arun Kumar**

GitHub: [@arunkumardev-07](https://github.com/arunkumardev-07)

## Prerequisites

Install the following tools before running the project:

- Java Development Kit (JDK) 21
- Maven 3.9 or compatible version
- Node.js and npm
- MySQL Server 8
- Git

Verify your installations:

```powershell
java -version
mvn -version
node -v
npm -v
git --version
```

## Running the Project Locally

The application consists of three main components: the blockchain, Java backend, and web frontend.

### 1. Start the local blockchain

Open a PowerShell terminal:

```powershell
cd blockchain
npm install
npx hardhat node
```

Keep this terminal open. The local blockchain must remain running while you test the application.

### 2. Configure and start the Java backend

Open a second PowerShell terminal and navigate to the backend:

```powershell
cd backend/evoting-backend/evoting-backend
```

Configure your local MySQL database and blockchain connection using your local application configuration. Do not publish database passwords, private keys, or other credentials to GitHub.

Then start the backend:

```powershell
mvn spring-boot:run
```

Use the port configured in your local application settings. This project has been tested locally on port 8081.

### 3. Start the frontend

Open a third PowerShell terminal:

```powershell
cd frontend
npx http-server -p 5500
```

If prompted to install `http-server`, approve only if you intend to use that package.

Open the frontend URL shown by the terminal, usually:

http://localhost:5500

### 4. Verify the application

- Confirm the local blockchain node is running.
- Confirm the Spring Boot backend starts successfully.
- Confirm MySQL is running and the database configuration is correct.
- Open the frontend and test the available voting and results features.

## Important Notes

- The local blockchain resets when its node is restarted; redeploy the contract and update local configuration as needed.
- Contract addresses and local database settings can differ between environments.
- Never use real funds or real voter information for testing.
- These instructions describe a local development setup, not a production deployment.

## Application Screenshots

### Login

![Login screen](screenshots/login.png)

### Voting Interface

![Voting interface](screenshots/voting.png)

### Election Results

![Election results](screenshots/results.png)
