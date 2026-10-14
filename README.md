\# Secure Blockchain-Based E-Voting System



\## Project Overview

A demonstration electronic voting system built using Java, Spring Boot, MySQL, Solidity, and Hardhat. It combines voter authentication, REST APIs, smart-contract-based vote recording, and a web interface to demonstrate blockchain integration.



\## Technologies Used

\- \*\*Backend:\*\* Java 21, Spring Boot, Maven

\- \*\*Database:\*\* MySQL 8

\- \*\*Blockchain:\*\* Solidity, Hardhat, local Ethereum-compatible network

\- \*\*Frontend:\*\* HTML, CSS, JavaScript

\- \*\*Security:\*\* BCrypt password hashing and SHA-256 cryptographic utilities



\## Key Features

\- Voter registration and login

\- Password hashing with BCrypt

\- REST APIs for voter and election operations

\- Smart-contract-based vote recording on a local blockchain

\- Prevention of duplicate voting in the application

\- Candidate vote counts retrieved from the blockchain

\- Web interface with voting and results dashboard



\## Project Structure

\- `backend/` — Spring Boot application and database integration

\- `blockchain/` — Solidity contracts and Hardhat scripts

\- `frontend/` — Web interface and JavaScript API integration



\## How It Works

1\. A voter registers and logs in.

2\. The application displays the available candidates.

3\. The voter submits a vote through the backend API.

4\. The backend validates the election and voter eligibility.

5\. The application submits a transaction to the local blockchain.

6\. The results dashboard retrieves candidate vote counts from the smart contract.



\## Running the Project

Run MySQL, the local Hardhat blockchain, the Spring Boot backend, and the frontend server. Configure the database connection and local blockchain settings before starting the application.



\## Important Note

This is an academic demonstration project intended for local testing. It is not designed or audited for real government elections. Production voting systems require stronger ballot privacy, independent security audits, access controls, and robust operational safeguards.

