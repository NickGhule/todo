# Todo Application

The Todo Application is a microservices-based project designed to manage tasks efficiently. It is built using Spring Boot and follows a modular architecture. The project includes multiple services, each responsible for specific functionalities.

## Modules

### 1. **todo-api-gateway**
- **Description**: Acts as the entry point for all client requests. It routes requests to the appropriate microservices.
- **Technologies**: Spring Cloud Gateway, Spring Security.
- **Key Features**:
  - API routing and load balancing.
  - OAuth2 resource server for securing APIs.
  - OpenAPI documentation.

### 2. **auth-service**
- **Description**: Handles user authentication and registration.
- **Technologies**: Spring Security, OAuth2, JWT.
- **Key Features**:
  - User login and registration.
  - OAuth2 login with external providers.
  - JWT token generation and validation.

### 3. **todo-task-service**
- **Description**: Manages tasks for users.
- **Technologies**: Spring Data JPA, PostgreSQL.
- **Key Features**:
  - CRUD operations for tasks.
  - Integration with the database for task persistence.

### 4. **common-lib**
- **Description**: A shared library containing common utilities and DTOs used across services.
- **Technologies**: Java.
- **Key Features**:
  - JWT utility for token management.
  - Shared DTOs for consistent data transfer.

## Architecture

The project follows a microservices architecture with the following key components:
- **API Gateway**: Centralized routing and security.
- **Authentication Service**: Manages user authentication and authorization.
- **Task Service**: Handles task-related operations.
- **Common Library**: Provides shared functionality across services.

## Prerequisites

- **Java**: OpenJDK 23 or higher.
- **Maven**: 3.8.0 or higher.
- **PostgreSQL**: Database for task persistence.
- **Environment Variables**:
  - `JWT_AUTH_SECRET_KEY`: Secret key for JWT token signing.

## How to Run

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd todo
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Start the services:
   - Navigate to each module directory and run:
     ```bash
     mvn spring-boot:run
     ```

4. Access the application:
   - API Gateway: `http://localhost:8080`
   - Auth Service: `http://localhost:8081/auth`
   - Task Service: `http://localhost:8082/tasks`

## API Documentation

Each service provides OpenAPI documentation accessible at `/swagger-ui.html`.

## Project Structure

```
todo/
├── auth-service/          # Authentication service
├── common-lib/            # Shared library
├── todo-api-gateway/      # API Gateway
├── todo-task-service/     # Task management service
├── pom.xml                # Parent POM
└── README.md              # Project documentation
```

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request.

## License

This project is licensed under the MIT License.
