# Inventory API

A Spring Boot inventory application with a browser-based HTML interface and REST API. It supports CRUD operations, searchable and pageable listings, and low-stock reporting.

## Use Case

The `Product` entity contains six fields:

| Field | Description |
| --- | --- |
| `id` | Database-generated identifier |
| `name` | Product name |
| `sku` | Unique stock-keeping unit |
| `category` | Product category |
| `price` | Unit price in Indian rupees (INR), stored with two decimal places |
| `quantity` | Available units; must be zero or greater |

## Technologies

- Java 17 and Spring Boot
- Gradle
- Spring Web MVC
- Spring Data JPA and Hibernate
- MySQL Connector/J
- Jakarta Bean Validation

## API Endpoints

All endpoints are under `/api/products` and exchange JSON.

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/products` | Create a product; returns `201 Created` and a `Location` header |
| `GET` | `/api/products` | List products with optional filters and pagination |
| `GET` | `/api/products/{id}` | Get one product |
| `PUT` | `/api/products/{id}` | Replace a product's editable fields |
| `DELETE` | `/api/products/{id}` | Delete a product; returns `204 No Content` |
| `GET` | `/api/products/low-stock?threshold=5` | Find products at or below the stock threshold |

Create and update request body:

```json
{
  "name": "Wireless Mouse",
  "sku": "MOU-1001",
  "category": "Accessories",
  "price": 24.99,
  "quantity": 18
}
```

`name`, `sku`, and `category` are required. `price` must be at least `0.01` and have at most two fractional digits. `quantity` must be zero or greater. SKUs must be unique.

Prices are displayed in Indian rupees (INR).

List supports `name` (case-insensitive partial match), `category` (case-insensitive exact match), `page`, `size`, and `sort`. For example:

```text
GET /api/products?name=mouse&category=Accessories&page=0&size=10&sort=price,desc
```

The default page size is 20, sorted by name ascending. Responses use Spring's paged JSON representation. Errors return a JSON object containing `timestamp`, `status`, `error`, `message`, and `path`.

## Additional Features

- Derived repository queries for product name and category filtering.
- A custom JPQL `@Query` for low-stock results.
- Pagination and client-selected sorting through Spring Data `Pageable`.
- Global exception handling for missing products, validation failures, and duplicate SKUs.

## Run Locally

Prerequisites: JDK 17 or newer and a running MySQL server. Create the database before starting the application:

```sql
CREATE DATABASE inventory_db;
```

The defaults target `localhost:3306`, user `root`, and password `change-me`. Override them with `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` as needed. For a quick local demo without MySQL, activate the included in-memory H2 profile:

```bash
./gradlew bootRun --args='--spring.profiles.active=demo'
```

The demo database is temporary and resets whenever the app restarts. It loads illustrative sample products for Books, Movies, Expenses, Products, Inventory, Tasks, Employees, and Recipes. To connect to MySQL instead:

```bash
DB_USERNAME=inventory_user DB_PASSWORD=your-password ./gradlew bootRun
```

Open `http://localhost:8080/` for the inventory interface. It supports product search, category filtering, sorting, pagination, low-stock filtering, and create, edit, and delete actions.

The wrapper may need network access the first time it downloads Gradle. Run tests with `bash gradlew test`.

## Docker

The root `Dockerfile` builds the application with Gradle and packages it in a Java 17 runtime image. Build and run the image with Docker after providing a reachable MySQL database:

```bash
docker build -t inventory-api .
docker run --rm -p 8080:8080 \
  -e DB_URL='jdbc:mysql://host.docker.internal:3306/inventory_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC' \
  -e DB_USERNAME=inventory_user \
  -e DB_PASSWORD=your-password \
  inventory-api
```

The Docker image does not include or start MySQL; configure the connection to an existing database service.