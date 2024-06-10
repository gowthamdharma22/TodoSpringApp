# Todo Backend Application

This is a Java-based Todo backend application built using Spring Boot. The application includes JWT-based authentication and authorization, email verification for user registration, and uses JPA for database operations with MySQL.

## Features

- User registration with email verification
- JWT-based authentication and authorization
- CRUD operations for Todo tasks
- Password reset functionality with email verification
- CORS configuration for frontend integration

## Technologies Used

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- JWT
- Jakarta Mail (JavaMail)
- Lombok

## Prerequisites

- Java 11 or higher
- Maven
- MySQL

## Getting Started

### Clone the Repository

```sh
git clone https://github.com/your-username/todo-backend.git
cd todo-backend
```

### Setup MySQL Database

1. Create a new database in MySQL.

```sql
CREATE DATABASE todo;
```

2. Update the `application.properties` file with your MySQL database credentials.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/todo
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=youremail@gmail.com
spring.mail.password=your-email-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

ALLOWED_ORIGIN=http://localhost:5173
```

### Build and Run the Application

```sh
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8008`.

### Configure Email Sending

To enable email sending functionality, you need to configure an email account. This example uses a Gmail account.

1. Enable "Less secure app access" on your Gmail account settings.
2. Update the `spring.mail.username` and `spring.mail.password` properties in `application.properties` with your Gmail account credentials.

### Project Structure

The project structure is as follows:

```
src/
├── main/
│   ├── java/
│   │   └── com/gd/todo/
│   │       ├── config/          # Configuration classes
│   │       ├── controller/      # REST controllers
│   │       ├── dao/             # Dao classes
│   │       ├── entity/          # JPA entities
│   │       ├── filters/         # JWT filters
│   │       ├── events/          # Event classes
│   │       ├── exceptions/      # Exception classes
│   │       ├── helper/          # Helper classes
│   │       ├── model/           # Model classes
│   │       ├── responseEntity/  # ResponseEntity classes
│   │       ├── repository/      # JPA repositories
│   │       ├── service/         # Service classes
│   │       └── util/            # Utility classes
│   └── resources/
└──       ├── application.properties  # Application properties
```

## API Endpoints

### To test Backend

- `/` - Welcome message from Backend

### Authentication and Authorization

- `POST /register` - Register a new user
- `GET /verifyRegistration` - Verify user registration via email token
- `POST /login` - Authenticate user and return JWT
- `POST /resetPassword` - Request password reset
- `POST /verifyPasswordReset` - Verify password reset token and change password
- `POST /changePassword` - Change password with old password to new one
- `POST /resendVerification` - Resend verification link to the email

### Todo Management

- `GET /todo/getAll` - Get all todos (Authenticated users only)
- `POST /todo/add` - Create a new todo (Authenticated users only)
- `PUT /todo/{id}` - Update a todo (Authenticated users only)
- `DELETE /todo/{id}` - Delete a todo (Authenticated users only)

## Security

The application uses JWT for securing the API endpoints. Spring Security is configured to protect the endpoints and handle JWT authentication. Only registered and authenticated users can access the Todo management endpoints.

### JWT Filter Implementation

The JWT filter is used to intercept incoming HTTP requests and validate the JWT token.
