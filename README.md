# STraderBot

STraderBot is a Java-based trading bot that uses the OANDA API to execute trading strategies.

## Project Status

This project is currently under development. The OANDA API usage is being updated from v1 to v20.

## Getting Started

### Prerequisites

* Java 8
* Maven

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/STraderBot.git
   ```
2. Navigate to the project directory:
   ```bash
   cd STraderBot
   ```
3. Install the dependencies:
   ```bash
   mvn install
   ```

### Configuration

1. Open `src/main/resources/application.properties`.
2. Add your OANDA account ID and access token:
   ```properties
   oanda.accountId=your-account-id
   oanda.accessToken=your-access-token
   ```

### Usage

Run the application:
```bash
mvn spring-boot:run
```