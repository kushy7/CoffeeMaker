# CoffeeMaker
## Setup
To run CoffeeMaker for the first time, ensure that the 'src/main/resources/application.yml' and 'src/resources/application.properties' files have been copied from their .tmp counterparts (and appropriately filled out with database credentials/a staff signup key).


*Line Coverage (should be >=70%)*

![Coverage](badges/badgeImg/jacoco.svg)

*Branch Coverage (should be >=50%)*

![Branches](badges/badgeImg/branches.svg)



## Tech Stack

- **Java 11**: Primary programming language for backend development.
- **Spring Boot**: Framework for building RESTful APIs and managing application configuration.
- **Spring Data JPA**: ORM layer for database access and CRUD operations.
- **JUnit & Spring Test**: Automated unit and integration testing.
- **Maven**: Build automation and dependency management.
- **MariaDB**: Relational database for persistent data storage.
- **Thymeleaf**: Server-side Java template engine for rendering dynamic HTML.
- **AngularJS**: Frontend JavaScript framework for dynamic user interfaces.
- **Bootstrap**: CSS framework for responsive and mobile-first UI design.
- **Docker**: Containerization for consistent development and deployment environments.
- **GitHub Actions**: CI/CD pipeline for automated testing and code quality checks.

## Application Overview

CoffeeMaker is a full-stack, enterprise-grade web application designed for managing a coffee shop’s operations. The system supports user authentication, session management, and role-based access control for both customers and staff. Key features include:

- **User Management**: Secure login, registration, and session handling for customers and staff.
- **Inventory Management**: Real-time tracking and updating of ingredients and inventory levels.
- **Recipe Management**: CRUD operations for coffee recipes, including ingredient management.
- **Order Processing**: Workflow for placing, fulfilling, and tracking coffee orders.
- **RESTful API**: Exposes endpoints for all major operations, enabling integration and automation.
- **Responsive UI**: Modern, mobile-friendly interface for both customers and staff.
- **Automated Testing & Code Quality**: Comprehensive test coverage and static analysis integrated into the CI/CD pipeline.

This architecture ensures scalability, maintainability, and security, making CoffeeMaker suitable for production environments and extensible for future enhancements.




