# E-Commerce Spring Boot Backend

A robust and scalable RESTful e-commerce backend built with **Spring Boot** and **Java 21**. This application provides a comprehensive API for managing users, products, orders, shopping carts, and secure payments processing, serving as the core backend for modern e-commerce platforms.

## 🚀 Features

*   **User Authentication & Authorization**: Secure login and registration powered by **Spring Security** and **JWT** (JSON Web Tokens).
*   **User Management**: REST endpoints for managing user profiles and addresses.
*   **Product Catalog Management**: Admin endpoints for structuring categories and managing products (CRUD operations).
*   **Shopping Cart Functionality**: Add products, track cart items, and calculate totals.
*   **Order Processing**: Place orders, view order history, and manage order lifecycles (admin and user levels).
*   **Payment Integration**: Seamless and secure payment processing integrated with the **Stripe API**.
*   **Reviews & Ratings**: Customers can leave ratings and reviews for previously purchased products.
*   **Database Management**: Relational data mapping with **Spring Data JPA** and **MySQL**.

## 🛠️ Tech Stack

*   **Core**: Java 21, Spring Boot 3.5.4
*   **Web**: Spring Web, Spring WebFlux
*   **Data Access**: Spring Data JPA, MySQL Connector J
*   **Security**: Spring Security, JJWT (JSON Web Tokens API)
*   **Payments**: Stripe Java SDK
*   **Utilities**: Lombok, Spring Boot Validation

## 📂 Project Architecture

Organized using a standard layered architecture:

*   `config`: Security configurations, JWT providers, and validators.
*   `controller`: REST API endpoints handling incoming HTTP requests.
*   `exception`: Global and custom exception handling (e.g., UserException, ProductException).
*   `model`: JPA Entities defining database tables (e.g., User, Product, Order, Cart, Category).
*   `repo`: Data access layer with Spring Data JPA Repositories.
*   `request`/`response`: Data Transfer Objects (DTOs) for structured payload formatting.
*   `service`: Core business logic and interface implementations.

## ⚙️ Setup & Installation

### Prerequisites
*   [Java 21 or higher](https://www.oracle.com/java/technologies/downloads/)
*   [Maven](https://maven.apache.org/download.cgi)
*   [MySQL](https://dev.mysql.com/downloads/installer/)

### 1. Clone the repository
```bash
git clone https://github.com/CoderVJain/ecommerce-backend.git
cd ecommerce-backend
```

### 2. Configure the Environment Variables & Keys
Since the project relies on environment variables, set up the following environment variables (or supply them in your IDE/system):

*   `PORT=5454` (or your preferred server port)
*   `DB_HOST=localhost`
*   `DB_PORT=3306`
*   `DB_NAME=ecommerceSpring` (Create this database in MySQL first: `CREATE DATABASE ecommerceSpring;`)
*   `DB_USERNAME=root` (Your MySQL username)
*   `DB_PASSWORD=your_mysql_password`

**Required Secrets:**
1. **Stripe API Key:** Open `src/main/resources/application.properties` and replace `<your-stripe-secret-key>` with your actual Stripe Secret Key.
2. **JWT Secret:** Open `src/main/java/com/example/E_CommerceSpring/config/JwtConstant.java` and replace `[ENCRYPTION_KEY]` with a secure, random string that will be used to sign your JSON Web Tokens.

### 3. Build and Run the Application
You can use the included Maven wrapper to build and run the application:

```bash
# Clean and install dependencies
./mvnw clean install

# Run the Spring Boot application (ensure env variables are set)
./mvnw spring-boot:run
```

By default (if you set `PORT=5454`), the base API URL will be: `http://localhost:5454/`

## 🔐 API Endpoints Overview

The backend uses standard RESTful routing. Example controller routing:
*   `/auth` - Authentication routes (Login, Register)
*   `/api/users` - User profile handling
*   `/api/products` - Product fetching
*   `/api/admin/products` - Admin product CRUD
*   `/api/cart` - Cart management
*   `/api/orders` - Order placements
*   `/api/payments` - Stripe payment links

