# 🏦 BFSI Banking Microservices Application

A **Banking, Financial Services, and Insurance (BFSI)** backend application built using **Spring Boot Microservices architecture**.
This project demonstrates **secure authentication, authorization, and transaction handling** using **JWT**, **Spring Security**, and **API Gateway**.

This project is built with **real-world banking standards**.

---

## 🚀 Tech Stack

* Java 17
* Spring Boot
* Spring Security
* Spring Cloud Gateway
* JWT (JSON Web Token)
* BCrypt Password Encoder
* Spring Data JPA
* MySQL 
* Maven

---

## 🧱 Microservices Architecture

```
Client
  |
  |-----> API Gateway
            |
            |-----> Auth Service
            |-----> User Service
            |-----> Transaction Service
```

✅ API Gateway as single entry point

---

## 🔐 JWT Authentication Flow

1. User registers using **User Service**
2. Password is encrypted using **BCrypt**
3. User logs in via **Auth Service**
4. Auth Service generates **JWT token**
5. Client sends JWT token in `Authorization` header
6. **API Gateway validates JWT**
7. Gateway forwards request to target microservice
8. Microservice processes request securely

---

## 📦 Microservices Details

---

### 🔑 Auth Service

Handles **authentication and JWT generation**.

**Responsibilities**

* User login
* JWT token generation
* Token validation logic

**Endpoints**

```http
POST /auth/login
```

---

### 👤 User Service

Handles **user registration and user data**.

**Responsibilities**

* User registration
* Password encryption using BCrypt
* Persisting user details

**Endpoints**

```http
POST /users/register
GET  /users/{id}
```

---

### 💸 Transaction Service

Handles **banking transactions**.

**Responsibilities**

* Debit transactions
* Credit transactions
* Fetch transaction history
* Accessible only via authenticated Gateway requests

**Endpoints**

```http
POST /transactions/debit
POST /transactions/credit
GET  /transactions/history
```

---

### 🌐 API Gateway

Acts as the **single entry point** for all requests.

**Responsibilities**

* Routing requests to microservices
* JWT validation
* Blocking unauthorized access
* Forwarding authenticated requests

**JWT Filter Flow**

* Extract token from `Authorization` header
* Validate token signature and expiration
* Reject request if token is invalid or missing

---

## 🛡️ Why JWT Validation at Gateway?

* Centralized authentication
* No duplicate security logic in services
* Stateless and scalable
* Production-grade security design

> Downstream services **trust the Gateway** and do not re-validate JWT.

---

## 🔄 Sample Request Flow

```
1. Client → POST /auth/login
2. Auth Service → JWT Token
3. Client → GET /transactions/history (with token)
4. Gateway → JWT Validation
5. Gateway → Forward request
6. Transaction Service → Response
```

---

## ⚙️ Security Configuration

* Spring Security configured using `SecurityFilterChain`
* CSRF disabled for APIs
* Public endpoints:

```
/auth/**
/users/register
```

* All other endpoints require JWT authentication

---

## 🧪 API Testing

Use **Postman** or similar tools.

Add header:

```http
Authorization: Bearer <JWT_TOKEN>
```

---

## 📂 Project Structure

```
bfsi-parent
│
├── api-gateway
├── auth-service
├── user-service
├── transaction-service
```

---

## 🎯 Key Concepts Implemented

* Spring Boot Microservices
* API Gateway pattern
* JWT-based authentication
* Spring Security
* BFSI transaction flow
* Secure microservice communication

---

## 🚧 Future Enhancements

* Role-based authorization (USER / ADMIN)
* Transaction validation rules
* Centralized exception handling
* Docker & Kubernetes deployment
* Logging and monitoring

---

## 👨‍💻 Author

**Asim Sutar**
Java | Spring Boot | Microservices | BFSI Domain

---

## ⭐ Support

If you found this project useful, please ⭐ star the repository!

---

> This project is built for **learning, interviews, and real-world backend development**.
