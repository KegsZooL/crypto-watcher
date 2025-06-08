<p aligin="center">
  <img src="https://github.com/user-attachments/assets/3ecf0580-5186-49f9-9ffa-c28898f3587c" alt= "Logo" />
</p>

# 👀 Crypto Watcher
Crypto Watcher is a Telegram bot built in Java using the Spring Boot (Web/Data) frameworks for tracking cryptocurrency prices.
The system follows a microservice architecture and consists of three independent services:

## 🧩 Microservices
1) **Dispatcher** – Handles interaction with the [Telegram API](https://core.telegram.org/) via the [Telegram Bots](https://github.com/rubenlagus/TelegramBots?tab=readme-ov-file) wrapper.
2) **Exchange Proxy** - proxy between the [OKX](https://www.okx.com) exchange and users. Processes cryptocurrency data.
3) **Data Manager** - Responsible for processing user data and interacting with the database.

## 📝 Features
-  **Receiving exchange data on cryptocurrencies via WebSocket/REST**
-  **Easy configuration for Reply/Inline keyboard markup**
-  **Fully customizable interactive menus through built-in entities**
-  **Multilingual interface with dynamic language switching**

![preview](https://github.com/user-attachments/assets/5a08a184-ab09-441b-b1b9-e5f424d6d719)

## ⚡️ Requirements
- **Java 17+**
- **Maven**
- **Docker & Docker Compose**
- **Git**

## 📦 Installation

### 1. Clone the Repository

```sh
git clone https://github.com/KegsZooL/crypto-watcher/
cd crypto-watcher
```

### 2. Configure `.env` Files

❗There are **two required .env files** to properly configure the project:
#### 📁 dispatcher/.env

```.env
SERVER_IP=YOUR_SERVER_IP
TELEGRAM_BOT_TOKEN=YOUR_TELEGRAM_BOT_TOKEN

# RabbitMQ
RABBITMQ_HOSTNAME=${SERVER_IP}
RABBITMQ_USERNAME=YOUR_RABBITMQ_USERNAME
RABBITMQ_PASSWORD=YOUR_RABBITMQ_PASSWORD
RABBITMQ_HTTP_PORT=15672:15672
RABBITMQ_AMQP_PORT=5672:5672

RABBITMQ_EXCHANGE_NAME=main
```
- Replace all `YOUR_...` values with actual credentials or tokens.
- Use either internal (localhost) or external IP for `SERVER_IP`.
- You can obtain the `TELEGRAM_BOT_TOKEN` by creating a new bot using **@BotFather** on Telegram.

#### 📁 .env (project root)
```.env
RABBITMQ_USERNAME=YOUR_RABBITMQ_USERNAME
RABBITMQ_PASSWORD=YOUR_RABBITMQ_PASSWORD

DATABASE_USERNAME=YOUR_DATABASE_USERNAME
DATABASE_PASSWORD=YOUR_DATABASE_PASSWORD
DATABASE_NAME=YOUR_DATABASE_NAME
DATABASE_PORT=5432:5432

RABBITMQ_IMAGE=rabbitmq:3.13-management
```
### 3. Unified Docker Setup (Optional)

🗒️ If you prefer to run all services on a **single machine**, follow these steps:

#### a) Remove dispatcher/.env
In this case, the root .env will be used across all services.

#### b) Move the Dispatcher’s Docker config into the main docker-compose.yml
Update your main docker-compose.yml:

```docker-compose.yaml
services:
  dispatcher:
    build:
      context: .
      dockerfile: dispatcher/Dockerfile
    env_file:
      - .env

  exchange_proxy:
    build:
      context: .
      dockerfile: exchange_proxy/Dockerfile
    depends_on:
      - rabbitmq
    env_file:
      - .env

  data_manager:
    build:
      context: .
      dockerfile: data-manager/Dockerfile
    depends_on:
      - rabbitmq
      - postgres
    env_file:
      - .env

  postgres:
    image: postgres:17
    restart: always
    env_file:
      - .env
    environment:
      POSTGRES_DB: ${DATABASE_NAME}
      POSTGRES_USER: ${DATABASE_USERNAME}
      POSTGRES_PASSWORD: ${DATABASE_PASSWORD}
    ports:
      - ${DATABASE_PORT}
    volumes:
      - ./postgres-data:/var/lib/postgresql/data

  rabbitmq:
    image: ${RABBITMQ_IMAGE}
    hostname: ${RABBITMQ_HOSTNAME}
    env_file:
      - .env
    environment:
      - RABBITMQ_DEFAULT_USER=${RABBITMQ_USERNAME}
      - RABBITMQ_DEFAULT_PASS=${RABBITMQ_PASSWORD}
    ports:
      - ${RABBITMQ_HTTP_PORT}
      - ${RABBITMQ_AMQP_PORT}
    volumes:
      - ./rabbitmq-data:/var/lib/rabbitmq/mnesia
```
### 🚀 4. Run the System
Use Docker Compose to build and run all services:
```sh
docker-compose up --build
```
- This will start:
  + Dispatcher
  + Exchange Proxy
  + Data Manager
  + RabbitMQ (message broker)
  + PostgreSQL (data storage)

## ☠️ Security Warning
NEVER commit `.env` files to version control. Always add them to `.gitignore`.
