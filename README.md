# Order Management Service

Order Management Service is a Spring Boot REST API for managing customers and orders.  
The application supports CRUD operations, filtered listing with pagination, CSV report generation, and bulk JSON upload.  
Database schema and seed data are managed using Liquibase.

---

## Tech Stack
- Java 21
- Spring Boot (Web, Validation, Data JPA)
- PostgreSQL
- Hibernate / JPA Specifications
- Liquibase (YAML changelogs)
- Lombok
- Testing: Spring Boot Test, H2, datasource-proxy

---

## Architecture
The application follows a layered architecture:
- **Controller layer** – REST API endpoints
- **Service layer** – business logic and validation
- **Repository layer** – data access with Spring Data JPA
- **DTO layer** – request/response contracts
- **Exception handling** – centralized error handling using `@RestControllerAdvice`

---

## Features

### Customers
- Create, update, delete customers
- Case-insensitive uniqueness check for customer name
- Customer deletion is restricted if related orders exist

### Orders
- Create, update, delete orders
- Retrieve order by ID
- List orders with filters and pagination
- Generate CSV reports for filtered orders
- Bulk upload orders from JSON file with success/failure statistics

---

## Database & Migrations
Liquibase is used for database versioning and runs automatically on application startup.

Managed objects:
- `customers` table with unique `customer_name`
- `orders` table with foreign key `customer_id → customers(id)`
- Initial seed data for customers and orders

Changelog structure:
- `db.changelog-master.yaml`
- `changeset-create-tables.yaml`
- `changeset-insert-customers.yaml`
- `changeset-insert-orders.yaml`

---

## API Endpoints

### Customers (`/api/customers`)
- `GET /api/customers` – get all customers
- `POST /api/customers` – create customer
- `PUT /api/customers/{id}` – update customer
- `DELETE /api/customers/{id}` – delete customer

**Create customer example**
```json
{
  "customerName": "John"
}
```
### Orders (`/api/orders`)
- `POST /api/orders` – create order
- `GET /api/orders/{id}` – get order by ID
- `PUT /api/orders/{id}` – update order
- `DELETE /api/orders/{id}` – delete order

**Create order example**
```json
{
  "customerId": 1,
  "date": "2025-01-10",
  "totalPrice": 120.50,
  "products": "Milk, Bread, Honey"
}
```
Listing, Reporting & Upload

POST /api/orders/_list – list orders with filters and pagination

POST /api/orders/report – download CSV report

POST /api/orders/upload – bulk upload orders from JSON file

List orders request example

```json
{
  "customerId": 1,
  "products": "Milk",
  "page": 0,
  "size": 10
}
```
Validation & Error Handling

Input validation using Jakarta Validation annotations

Centralized exception handling with @RestControllerAdvice

Errors returned using ProblemDetail format

Handled cases include:

400 Bad Request – validation errors

404 Not Found – missing resources

409 Conflict – duplicate customer name or forbidden deletion

500 Internal Server Error – unexpected errors

How to Run:

Prerequisites

Java 21

PostgreSQL database

Configuration

Configure database connection in application.yml or application.properties.

Run application
```bash
./mvnw spring-boot:run
```
Application will start on: http://localhost:8080

Running Tests
```bash 
./mvnw test
```