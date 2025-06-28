# CoffeeMaker
## Setup
To run CoffeeMaker for the first time, ensure that the 'src/main/resources/application.yml' and 'src/resources/application.properties' files have been copied from their .tmp counterparts (and appropriately filled out with database credentials/a staff signup key).


*Line Coverage (should be >=70%)*

![Coverage](badges/badgeImg/jacoco.svg)

*Branch Coverage (should be >=50%)*

![Branches](badges/badgeImg/branches.svg)



## Tech Stack

- **Java 11**: Primary programming language for backend development.
- **Spring Boot 2.3.7**: Framework for building web applications and managing application configuration.
- **Spring Data JPA**: ORM layer for database access and CRUD operations with Hibernate.
- **JUnit 5 (Jupiter)**: Automated unit and integration testing framework.
- **Maven 3.6.3**: Build automation and dependency management with Maven Wrapper.
- **MySQL**: Relational database for persistent data storage (using MySQL Connector/J 8.0.22).
- **Thymeleaf**: Server-side Java template engine for rendering dynamic HTML.
- **AngularJS 1.6.4**: Frontend JavaScript framework for dynamic user interfaces.
- **Bootstrap**: CSS framework for responsive and mobile-first UI design.
- **GitHub Actions**: CI/CD pipeline for automated testing, code quality checks, and coverage reporting.
- **JaCoCo**: Code coverage analysis and reporting tool.
- **Checkstyle**: Static code analysis for maintaining code quality standards.
- **PMD**: Static analysis tool for detecting potential code issues.

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




