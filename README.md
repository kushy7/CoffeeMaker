# CoffeeMaker ☕

A modern, full-stack **enterprise-grade coffee shop management system** built with cutting-edge Java technologies and microservices architecture. CoffeeMaker provides a comprehensive solution for coffee shop operations with real-time inventory management, order processing, and customer relationship management.

## 🚀 Quick Start

### Prerequisites
- **Java 11+** (OpenJDK recommended)
- **MySQL 8.0+** 
- **Maven 3.6+** (or use included Maven Wrapper)
- **macOS/Linux/Windows** (tested on macOS)

### Installation & Setup

#### 1. Database Setup
```bash
# Install MySQL (macOS with Homebrew)
brew install mysql

# Start MySQL service
brew services start mysql

# Connect to MySQL and create database
mysql -u root -p
CREATE DATABASE CoffeeMaker;
exit;
```

#### 2. Application Configuration
```bash
# Navigate to project directory
cd CoffeeMaker-Individual/src/main/resources/

# Copy configuration templates
cp application.yml.template application.yml
cp application.properties.template application.properties

# Edit application.properties to set staff signup key
# Default: auth.staffSignupKey=admin123
```

#### 3. Build & Run
```bash
# Navigate to project root
cd CoffeeMaker-Individual/

# Make Maven wrapper executable (if needed)
chmod +x ./mvnw

# Run the application
./mvnw spring-boot:run
```

#### 4. Access the Application
- **Web Interface**: http://localhost:8080
- **API Endpoints**: http://localhost:8080/api/
- **Database**: MySQL on localhost:3306

### 🔧 Configuration Details

The application uses Spring Boot's auto-configuration with the following key settings:

**Database Configuration** (`application.yml`):
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/CoffeeMaker?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&serverTimezone=EST
    username: root
    password: 
  jpa:
    hibernate:
      ddl-auto: update
      dialect: org.hibernate.dialect.MySQL5Dialect
```

**Security Configuration** (`application.properties`):
```properties
auth.staffSignupKey=admin123  (or any chosen password to initialize staff member accounts)
```

### 🗄️ Database Management

#### View Database Contents
```bash
# Connect to MySQL
mysql -u root -p

# Use CoffeeMaker database
USE CoffeeMaker;

# List all tables
SHOW TABLES;

# View specific table data
SELECT * FROM staff;
SELECT * FROM customer;
SELECT * FROM recipe;
SELECT * FROM ingredient;
SELECT * FROM inventory;
```

#### Database Schema
The application automatically creates the following tables:
- `customer` - Customer account information
- `staff` - Staff user accounts and permissions
- `recipe` - Coffee recipes and pricing
- `ingredient` - Available ingredients
- `inventory` - Current inventory levels
- `customer_order` - Order tracking and status
- `session` - User session management


*Line Coverage (should be >=70%)*

![Coverage](badges/badgeImg/jacoco.svg)

*Branch Coverage (should be >=50%)*

![Branches](badges/badgeImg/branches.svg)



## 🛠️ Tech Stack & Architecture

### Backend Technologies
- **Java 11** - Enterprise-grade, object-oriented programming language with strong typing and memory management
- **Spring Boot 2.3.7** - Microservices-ready framework with auto-configuration, embedded servers, and production-ready features
- **Spring Data JPA** - Advanced ORM layer with repository pattern, query methods, and transaction management
- **Hibernate 5.4** - High-performance object-relational mapping with lazy loading and caching
- **Spring Security** - Comprehensive security framework for authentication and authorization
- **Spring Web MVC** - RESTful web services and MVC architecture with content negotiation

### Database & Persistence
- **MySQL 8.0+** - ACID-compliant relational database with high performance and scalability
- **MySQL Connector/J 8.0.22** - Official JDBC driver with connection pooling and SSL support
- **HikariCP** - High-performance JDBC connection pool for optimal database performance

### Frontend & UI
- **Thymeleaf** - Modern server-side template engine with natural templating and Spring integration
- **AngularJS 1.6.4** - Dynamic single-page application framework with two-way data binding
- **Bootstrap 4** - Responsive, mobile-first CSS framework with component library
- **HTML5/CSS3** - Modern web standards with semantic markup and advanced styling

### Testing & Quality Assurance
- **JUnit 5 (Jupiter)** - Next-generation testing framework with parameterized tests and extensions
- **Spring Boot Test** - Integration testing with test slices and auto-configuration
- **Mockito** - Mocking framework for unit testing with behavior verification
- **JaCoCo** - Code coverage analysis with branch and line coverage reporting
- **Checkstyle** - Static code analysis for coding standards and best practices
- **PMD** - Source code analyzer for detecting bugs and code smells

### DevOps & CI/CD
- **Maven 3.6+** - Build automation with dependency management and multi-module support
- **Maven Wrapper** - Reproducible builds with embedded Maven distribution
- **GitHub Actions** - Continuous integration with automated testing and deployment
- **Docker Ready** - Containerization support for microservices deployment

### Development Tools
- **Spring Boot DevTools** - Hot reloading and development productivity features
- **Spring Boot Actuator** - Production monitoring and health checks
- **H2 Database** - In-memory database for testing and development
- **Lombok** - Boilerplate code reduction with annotations

## Application Overview

CoffeeMaker is a full-stack web application designed for managing a coffee shop's operations. The system supports user authentication, session management, and role-based access control for both customers and staff. Key features include:

- **User Management**: Secure login, registration, and session handling for customers and staff with role-based access control.
- **Inventory Management**: Real-time tracking and updating of ingredients and inventory levels with automatic stock management.
- **Recipe Management**: CRUD operations for coffee recipes, including ingredient management and recipe validation.
- **Order Processing**: Workflow for placing, fulfilling, and tracking coffee orders with status management.
- **RESTful API**: Comprehensive API endpoints for all major operations, enabling integration and automation.
- **Responsive UI**: Modern, mobile-friendly interface built with Bootstrap and AngularJS for both customers and staff.
- **Automated Testing & Code Quality**: Comprehensive test coverage with JUnit 5, static analysis with Checkstyle and PMD, and code coverage reporting with JaCoCo integrated into the CI/CD pipeline.
- **Database Integration**: MySQL database with JPA/Hibernate for data persistence and transaction management.

This architecture ensures maintainability and code quality through automated testing, static analysis, and comprehensive coverage reporting, making CoffeeMaker suitable for educational and development environments with extensible design for future enhancements.

## 🐛 Troubleshooting

### Common Issues & Solutions

#### 1. Permission Denied Error
```bash
# Error: zsh: permission denied: ./mvnw
# Solution: Make Maven wrapper executable
chmod +x ./mvnw
```

#### 2. Database Connection Failed
```bash
# Error: Failed to determine a suitable driver class
# Solution: Ensure MySQL is running and database exists
brew services start mysql
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS CoffeeMaker;"
```

#### 3. Port Already in Use
```bash
# Error: Port 8080 is already in use
# Solution: Kill process using port 8080
lsof -ti:8080 | xargs kill -9
# Or change port in application.yml
```

#### 4. Configuration File Missing
```bash
# Error: No active profile set
# Solution: Copy configuration templates
cp src/main/resources/application.yml.template src/main/resources/application.yml
cp src/main/resources/application.properties.template src/main/resources/application.properties
```

### Development Commands

#### Build & Test
```bash
# Clean and compile
./mvnw clean compile

# Run tests
./mvnw test

# Package application
./mvnw package

# Run with specific profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```


### Code Quality Standards
- Maintain test coverage above 70%
- Follow Checkstyle guidelines
- Write comprehensive unit and integration tests
- Update documentation for new features



