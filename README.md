# Warehouse Sensor Monitoring System

## Overview

This system comprises two microservices built on the Spring Boot framework, designed to monitor and process sensor data in a warehouse environment. The architecture leverages UDP communication, NATS messaging system, and containerized deployment for scalability and reliability.

## System Components

### 1. Sensor Data Acquisition Service (SDAS)

#### Technical Specifications:
- **Framework**: Spring Boot
- **Communication Protocol**: UDP (User Datagram Protocol)
- **Listening Ports**: 3344 and 3355
- **Concurrency Model**: Non-blocking I/O with multiple handlers per port
- **Message Format**: Key-value pairs (e.g., `sensor_id=t1; value=30`)
- **Data Transformation**: JSON serialization (e.g., `{"id":"t1","value":"30"}`)
- **Message Broker**: NATS (Neural Autonomic Transport System)
- **NATS Subjects**: `temperature` and `humidity`

#### Functionality:
1. Initializes UDP servers on ports 3344 and 3355 using non-blocking I/O.
2. Implements concurrent message handling with multiple threads per port.
3. Performs real-time validation of incoming sensor data against a predefined schema.
4. Executes message transformation from key-value format to JSON.
5. Publishes processed data to NATS subjects based on sensor type.

### 2. Central Monitoring and Alerting Service (CMAS)

#### Technical Specifications:
- **Framework**: Spring Boot
- **Data Source**: NATS subscription
- **Configuration Management**: Spring Configuration for threshold values
- **Alerting Mechanism**: Configurable thresholds for different sensor types

#### Functionality:
1. Establishes persistent subscriptions to NATS subjects (`temperature` and `humidity`).
2. Implements a real-time data processing pipeline for incoming sensor readings.
3. Compares sensor values against predefined thresholds stored in Spring Configuration.
4. Triggers alarms based on complex condition evaluation (e.g., value exceeding threshold).
5. Provides extensible architecture for adding more sensor types and alert conditions.

### 3. System Orchestration and Deployment

#### Technical Stack:
- **Containerization**: Docker
- **Orchestration**: Docker Compose
- **Build Automation**: Maven
- **Scripting**: Bash

#### Deployment Process:
1. Utilizes `run-e2e-tests.sh` Bash script for end-to-end system deployment and testing.
2. Executes Maven build processes for both Spring Boot services.
3. Deploys containerized instances of Warehouse Service, Central Service, and NATS using Docker Compose.
4. Implements health checks to ensure all services are operational before proceeding.
5. Initializes UDP clients to simulate sensor data transmission on both configured ports.
6. Generates pseudo-random sensor data, including values designed to trigger alarm conditions.
7. Provides comprehensive logging and error handling throughout the deployment and testing process.

This architecture ensures a robust, scalable, and maintainable system for real-time sensor data processing and alerting in a warehouse environment, leveraging modern microservices patterns and messaging systems.