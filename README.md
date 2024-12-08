# Blogging Engine API 🎉️

👷 Under Development 🚀️

## Overview

The **Blogging Engine API** is a robust and scalable backend service designed to manage a blogging platform. It provides endpoints for user authentication, post management, and comment handling, ensuring a seamless experience for both content creators and readers.

## Architecture

The API follows a layered architecture, ensuring separation of concerns and promoting maintainability:

1. **Controller Layer**: Handles HTTP requests and responses.
2. **Service Layer**: Contains business logic.
3. **Repository Layer**: Manages data persistence using Spring Data JPA.
4. **Security Layer**: Manages authentication and authorization using Spring Security and JWT.

<br>

> UML Diagram

![](readmecontent/bloggingEngineAPI.png)

## Design Principles & Patterns

- **SOLID Principles**: Ensures a clean and maintainable codebase.
- **DTO Pattern**: Data Transfer Objects are used to encapsulate data and reduce coupling between layers.
- **Builder Pattern**: Used for constructing complex objects, ensuring immutability and readability.
- **Singleton Pattern**: Ensures a single instance of utility classes.

## Spring Boot Modules

- **Spring Boot Starter Web**: For building web applications and RESTful services.
- **Spring Boot Starter Data JPA**: For data persistence and ORM.
- **Spring Boot Starter Security**: For securing the application.
- **Spring Boot Starter Validation**: For validating request data.
- **Spring Boot Starter Test**: For testing the application.

## Features

- **User Authentication**: Register and login with JWT-based authentication.
- **Post Management**: Create, read, update, and delete blog posts.
- **Comment Management**: Add, view, and delete comments on posts.

## Getting Started

### Prerequisites

- Java 17
- Maven
- Docker

### Installation

1. **Clone the repository**:

   ```sh
   git clone https://github.com/valentinnsoare/blogging-engine-api.git
   cd blogging-engine-api
   ```
2. **Build the project**:

   ```sh
   mvn clean install
   ```
3. **Run the application**:

   ```sh
   mvn spring-boot:run
   ```
4. **Run the MySQL container**:

   ```sh
   docker-compose up -d
   ```

## Usage

### API Endpoints

- **Authentication**:

  - `POST /api/auth/login`: Login a user.
  - `POST /api/auth/register`: Register a new user.
- **Posts**:

  - `GET /api/posts`: Get all posts.
  - `POST /api/posts`: Create a new post.
  - `PUT /api/posts/{id}`: Update a post.
  - `DELETE /api/posts/{id}`: Delete a post.
- **Comments**:

  - `GET /api/posts/{postId}/comments`: Get comments for a post.
  - `POST /api/posts/{postId}/comments`: Add a comment to a post.
  - `DELETE /api/comments/{id}`: Delete a comment.

## Contributing

Contributions are welcome! Please fork the repository and create a pull request.

## License

This project is licensed under the MIT License.

## Contact

For any inquiries, please contact me www.linkedin.com/in/valentin-soare
