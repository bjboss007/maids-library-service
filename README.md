# Library Management System

The Library Management System is a web-based application designed to streamline library operations, allowing librarians to efficiently manage books, patrons, and borrowing records. This document provides an overview of the application, including its features, setup instructions, API endpoints, and security measures.

## Features

- **Book Management:**
    - Add, update, and delete books
    - View details of all books or a specific book by ID

- **Patron Management:**
    - Add, update, and delete patrons
    - View details of all patrons or a specific patron by ID

- **Borrowing Records:**
    - Allow patrons to borrow books
    - Record the return of borrowed books

## Technologies Used

The application is built using the following technologies:

- **Java:** The primary programming language
- **Spring Boot:** Framework for building robust and scalable web applications
- **Spring Data JPA:** Simplifies data access layer implementation
- **PostgreSQL:** Robust and scalable relational database for production
- **Docker Compose:** Tool for defining and running multi-container Docker applications
- **RESTful API:** Design architecture for creating scalable web services
- **Maven:** Dependency management and build automation tool

## Setup

To set up and run the application locally, follow these steps:

1. **Clone the repository:**
    ```bash
    git clone https://github.com/bjboss007/maids-library-service.git
    ```

2. **Navigate to the project directory:**
    ```bash
    cd maids-library-service
    ```

3. **Build the project:**
    ```bash
    mvn clean install
    ```

4. **Run the application:**
    ```bash
    mvn spring-boot:run
    ```

5. **Access the application:**
    - Open a web browser and go to: [http://localhost:8990](http://localhost:8990)

### Using Docker Compose:

1. **Clone the repository:**
    ```bash
    git clone https://github.com/bjboss007/maids-library-service.git
    ```

2. **Navigate to the project directory:**
    ```bash
    cd maids-library-service
    ```

3. **Start the application using Docker Compose:**
    ```bash
    docker-compose up
    ```

4. **Access the application:**
    - Open a web browser and go to: [http://localhost:8990](http://localhost:8990)

## API Endpoints

The application provides the following RESTful API endpoints:

- **Books:**
    - `GET /api/books`: Retrieve a list of all books
        - Sample Response Payload:
      ```json
      [
          {
              "id": 1,
              "title": "Rich Dad Poor Dad",
              "author": "Robert T. Kiyosaki",
              "publicationYear": 2022,
              "isbn": "ISBN-12345"
          },
          {
              "id": 2,
              "title": "Mastery",
              "author": "Robert Green",
              "publicationYear": 2021,
              "isbn": "ISBN-67890"
          }
      ]
      ```

    - `GET /api/books/{id}`: Retrieve details of a specific book by ID
        - Sample Response Payload:
      ```json
      {
          "id": 1,
          "title": "Rich Dad Poor Dad",
          "author": "Robert T. Kiyosaki",
          "publicationYear": 2022,
          "isbn": "ISBN-12345"
      }
      ```

    - `POST /api/books`: Add a new book to the library
        - Sample Request Payload:
      ```json
      {
          "title": "Seduction Volume 1",
          "author": "Robert Green",
          "publicationYear": 2023,
          "isbn": "ISBN-54321"
      }
      ```

    - `PUT /api/books/{id}`: Update an existing book's information
        - Sample Request Payload:
      ```json
      {
          "title": "Seduction Volume 1 edited",
          "author": "Robert Green",
          "publicationYear": 2023,
          "isbn": "ISBN-54321"
      }
      ```

    - `DELETE /api/books/{id}`: Remove a book from the library
  
- **Patrons:**
    - `GET /api/patrons`: Retrieve a list of all patrons
        - Sample Response Payload:
      ```json
      [
          {
              "id": 1,
              "name": "John Doe",
              "contactInformation": "john@example.com"
          },
          {
              "id": 2,
              "name": "Jane Smith",
              "contactInformation": "jane@example.com"
          }
      ]
      ```

    - `GET /api/patrons/{id}`: Retrieve details of a specific patron by ID
        - Sample Response Payload:
      ```json
      {
          "id": 1,
          "name": "John Doe",
          "contactInformation": "john@example.com"
      }
      ```

    - `POST /api/patrons`: Add a new patron to the system
        - Sample Request Payload:
      ```json
      {
          "name": "Kennedy Marron",
          "contactInformation": "kennedym@example.com"
      }
      ```

    - `PUT /api/patrons/{id}`: Update an existing patron's information
        - Sample Request Payload:
      ```json
      {
          "name": "Kennedy Marron",
          "contactInformation": "kennedym@gmail.com"
      }
      ```

    - `DELETE /api/patrons/{id}`: Remove a patron from the system

- **Borrowing:**
    - `POST /api/borrow/{bookId}/patron/{patronId}`: Allow a patron to borrow a book
    - `PUT /api/return/{bookId}/patron/{patronId}`: Record the return of a borrowed book by a patron

## Security

- **JWT-based Authorization:** Token-based authentication for securing API endpoints using JSON Web Tokens

- **Authentication Endpoint:**
    - `POST /api/auth/login`: Endpoint for user authentication and token generation

This endpoint can be used to authenticate users and generate JWT tokens, which can then be used to access other secured endpoints by including the token in the request headers.
### Login JSON Example:

To authenticate and generate a JWT token, send a POST request to `/api/auth/login` with the following JSON payload:

```json
{
    "username": "admin@maids.com",
    "password": "password"
}
```