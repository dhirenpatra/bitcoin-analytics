# BitCoinAPI

## Overview

BitCoinAPI is a Spring Boot application that provides an API to fetch and cache Bitcoin statistics. It includes endpoints to fetch Bitcoin data within a specified date range and store the results in a Redis cache to improve performance.

## Features

- Fetch Bitcoin statistics for a specified date range.
- Cache the fetched data using Redis.
- Retrieve cached data efficiently.

## Technologies Used

- Java 17
- Spring Boot 3.3.1
- Spring Data Redis
- Maven
- Docker

## Prerequisites

- Java 17
- Maven
- Docker

## Setup Instructions

### Backend Setup

1. **Clone the repository:**

    ```sh
    git clone https://github.com/yourusername/BitCoinAPI.git
    cd BitCoinAPI
    ```

2. **Build the application:**

    ```sh
    mvn clean package
    ```

3. **Run the application:**

    ```sh
    mvn spring-boot:run
    ```

4. **Run with Docker:**

    - **Build Docker image:**

        ```sh
        docker build -t bitcoin-analytics .
        ```

    - **Run Docker container:**

        ```sh
        docker run -p 8080:8080 bitcoin-analytics
        ```
      
   - **Run With Docker Compose:**

       ```sh
       docker-compose up
       ```

## Docker Configuration

### Docker Compose

To run the application with Redis, use the following `docker-compose.yml` file:

```yaml
version: '3.8'
services:
  bitcoin-api:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - redis
  redis:
    image: "redis:alpine"
    ports:
      - "6379:6379"
```