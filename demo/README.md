# 📘 Spring Boot Reporting & CRUD Enterprise Demo

![Java](https://img.shields.io/badge/Java-25-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen)
![JasperReports](https://img.shields.io/badge/JasperReports-PDF%20%2F%20XLSX-blue)

---

## 📌 Overview

This project is a **Spring Boot enterprise backend application** that provides:

- CRUD operations with soft delete support
- Dynamic filtering using Specification pattern
- JasperReports PDF and XLSX report generation
- Swagger/OpenAPI documentation
- DTO mapping using MapStruct
- Global exception handling
- Pageable pagination support

Designed using enterprise layered architecture for scalability and maintainability.

## Images

![MySQL](/images/mysql.png)
![Report Excel](/images/report-excel.png)
![Report PDF](/images/report-pdf.png)

---

## 🏗 Architecture

```
controller
service
repository
entity
dto
mapper
specification
search
exception
config
```

---

## 🚀 Technologies

- Java 25
- Spring Boot 4.1.0
- Spring Data JPA
- MapStruct
- JasperReports
- Swagger/OpenAPI (Springdoc)
- PostgreSQL / MySQL compatible
- Maven

---

## 📂 Project Structure

```
com.example.demo

├ controller
├ service
├ repository
├ entity
├ dto
├ mapper
├ specification
├ search
├ exception
├ config
└ report services
```

---

## 📊 Features

### Customer Management

✅ Create customers
✅ Update customers
✅ Soft delete customers
✅ Pagination support
✅ Dynamic search filtering

---

### Reporting System

✅ PDF report generation
✅ XLSX report export
✅ Dynamic filtering in reports
✅ Report metadata

Metadata includes:

- Report generation date
- User who generated report
- Total records

---

## 🔎 Dynamic Filtering

Uses SearchCriteria pattern.

Example request:

```json
[
  {
    "field": "email",
    "operation": "LIKE",
    "value": "email"
  }
]
```

---

## 📄 Reports Location

```
src/main/resources/reports/customer/
```

Files:

```
customer_report_pdf.jrxml
customer_report_pdf.jasper
customer_report_excel.jrxml
customer_report_excel.jasper
```

---

## 🧾 Report API

### Generate Report

```
POST /api/customers/report
```

Request Example:

```json
{
  "filters": [
    {
      "field": "email",
      "operation": "LIKE",
      "value": "@email.com"
    }
  ],
  "generatedBy": "admin",
  "format": "XLSX"
}
```

Supported formats:

- PDF
- XLSX

---

## 🌐 Swagger Documentation

Swagger UI:

```
http://localhost:8080/swagger-ui/index.html
```

OpenAPI Specs:

```
http://localhost:8080/v3/api-docs
```

---

## 🧪 API Endpoints

### Customers

| Method | Endpoint              | Description             |
| ------ | --------------------- | ----------------------- |
| GET    | /api/customers        | Get paginated customers |
| GET    | /api/customers/{id}   | Get customer by ID      |
| POST   | /api/customers        | Create customer         |
| PUT    | /api/customers/{id}   | Update customer         |
| DELETE | /api/customers/{id}   | Soft delete customer    |
| GET    | /api/customers/search | Dynamic search          |

---

### Reports

| Method | Endpoint              | Description              |
| ------ | --------------------- | ------------------------ |
| POST   | /api/customers/report | Generate customer report |

---

## ⚙ Configuration

Application profiles:

```
application.yaml
application-dev.yaml
application-prod.yaml
```

---

## 🧾 Database

The project contains sample data:

```
src/main/resources/data.sql
```

Automatically loaded when Spring Boot starts.

---

## 🐳 Docker Support (Production Ready)

This project includes a Dockerfile configured for production deployment.

The application can be containerized and deployed in cloud environments such as:

- AWS
- Azure
- Google Cloud
- Kubernetes clusters

---

## ▶ Running Project

### Build

```bash
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```

---

## 📦 API Testing

Recommended tools:

- Postman
- Insomnia
- Swagger UI

---

## 👨‍💻 Author

**Juan Ignacio Caprioli**

Full Stack Developer

- GitHub: https://github.com/ChanoChoca
- LinkedIn: https://www.linkedin.com/in/juan-ignacio-caprioli-2a645422b

---
