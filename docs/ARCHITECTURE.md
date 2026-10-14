# System Architecture

```text
Voter
  |
  v
Web Frontend (HTML, CSS, JavaScript)
  |
  v
Java Spring Boot Backend (REST APIs)
  |                    |
  v                    v
MySQL Database    Solidity Smart Contract
                        |
                        v
                  Local Blockchain
```

## Component Responsibilities

- Frontend: Provides the user interface.
- Java backend: Handles application requests.
- MySQL: Stores application data.
- Smart contract: Implements blockchain voting operations.
- Local blockchain: Supports development and testing.
