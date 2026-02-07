# SendaTrader76 Trading Bot

SendaTrader76 is an automated trading bot built with Java and Spring Boot, designed to execute trading strategies on the OANDA platform. The bot supports both live trading (via OANDA API) and backtesting modes. It features real-time data processing using WebSockets and an event-driven architecture.

## Features

- **Automated Trading**: Executes trades based on defined strategies.
- **Backtesting Support**: Test strategies against historical data before going live.
- **OANDA Integration**: Seamless connection to OANDA's Live and Practice APIs.
- **Real-time Data**: Streams price updates and positions via WebSockets (STOMP).
- **Technical Indicators**: Includes implementations for Bollinger Bands and Moving Averages (MACD and RSI are in development).
- **Event-Driven Architecture**: Uses Google Guava EventBus for decoupled component communication.

## Technology Stack

- **Language**: Java 23
- **Framework**: Spring Boot 3.4.2
- **Build Tool**: Maven
- **Libraries**:
  - Google Guava
  - Apache Commons Lang3
  - Google Gson
  - WebJars (Bootstrap, jQuery, SockJS, STOMP)

## Prerequisites

- **Java Development Kit (JDK) 23** or higher.
- **Maven 3.x** installed and configured.
- An **OANDA Account** (Practice or Live) for API access.

## Configuration

Configuration is managed via the `src/main/resources/application.properties` file.

| Property | Description | Example Value |
|---|---|---|
|strader76.strategy.test| Toggle between Backtesting (`true`) and Live/Practice (`false`). | `true` |
|oanda.account.id| Your OANDA Account ID. | `xxxx` |
|oanda.rest.header.authentication| Your OANDA API Access Token. | `Bearer <your-token>` |
|strader76.websocket.topic| WebSocket topic for broadcasting candlestick data. | `/topic/candlestick` |
|strader76.websocket.topic.positions| WebSocket topic for broadcasting position updates. | `/topic/positions` |

### Important Note on Security
**Do not commit your real OANDA API tokens or Account IDs to version control.** Use environment variables or a separate configuration file that is git-ignored for sensitive data.

## Installation & Running

1.  **Clone the repository**:
    ```bash
    git clone <repository-url>
    cd SendaTrader76
    ```

2.  **Build the project**:
    ```bash
    mvn clean install
    ```

3.  **Run the application**:
    You can run the application using the Spring Boot Maven plugin:
    ```bash
    mvn spring-boot:run
    ```
    Or by building a JAR and running it:
    ```bash
    java -jar target/STraderBot-0.1.jar
    ```

## Project Structure

The source code is organized as follows:

- `src/main/java/sendaTrader76/bot`: Root package.
    - `Application.java`: Main entry point.
    - `strategies`: Contains trading strategies (e.g., `BollingerStrategy`).
    - `indicators`: Contains technical indicators (`BollingerIndicator`, `MovingAverageIndicator`, etc.).
    - `services`: Core services for price streaming, account management, and backtesting.
    - `dto`: Data Transfer Objects for API communication.
    - `config`: Configuration classes.

## Development

- **Adding Strategies**: Implement the `Strategy` interface (or extend existing base classes) in the `strategies` package.
- **Adding Indicators**: Add new indicator logic in the `indicators` package.
- **Web Interface**: The project includes static resources in `src/main/resources/static` and `public` for a frontend dashboard (using Bootstrap/jQuery).

## License

[Insert License Information Here]
