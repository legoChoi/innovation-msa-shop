## MSA

---

## Services

| Service             | Description             | Skills                                    | Port         |
|---------------------|-------------------------|-------------------------------------------|--------------|
| **Eureka Server**   | 서비스 등록 및 헬스 체크          | Spring Cloud Netflix Eureka Server        | 19090        |
| **API Gateway**     | 외부 요청 라우팅, JWT 인증/인가 처리 | Spring Cloud Gateway, JWT                 | 19091        |
| **Order Service**   | 주문  관리                  | Spring Boot, JPA, MySQL, Redis, OpenFeign | 19092        |
| **Product Service** | 상품 관리                   | Spring Boot, JPA, MySQL, Redis, OpenFeign | 19093, 19094 |
| **Auth Service**    | 회원 가입, 로그인              | Spring Boot, JPA, MySQL, Redis, JWT       | 19095        |

---

## API Specification

- **Products**

| HTTP | Path       | Response Status | Description |               |
|------|------------|-----------------|-------------|---------------|
| POST | /products  | 201 CREATED     | 상품 추가       | REQUIRED-#1.1 |
| GET  | /products  | 200 OK          | 상품 목록 조회    | REQUIRED-#1.2 |

- **Products(Internal)**

| HTTP | Path                     | Description    |
|------|--------------------------|----------------|
| GET  | /internal/products/fail  | 서비스 에러(실패 케이스) |
| POST | /internal/products       | 상품 정합성 검증      |

- **Auth**

| HTTP | Path          | Response Status | Description |               |
|------|---------------|-----------------|-------------|---------------|
| POST | /auth/sign-in | 200 OK          | 로그인         | REQUIRED-#1.7 |
| POST | /auth/sign-up | 201 CREATED     | 회원 가입       | REQUIRED-#1.8 |

- **Orders**

| HTTP | Path              | Response Status         | Description   |                   |
|------|-------------------|-------------------------|---------------|-------------------|
| POST | /orders           | 201 CREATED             | 주문 추가         | REQUIRED-#1.3     |
| POST | /orders?fail      | 503 SERVICE UNAVAILABLE | 주문 추가(실패 케이스) | REQUIRED-#1.4     |
| PUT  | /orders/{orderId} | 200 OK                  | 주문 상품 추가      | REQUIRED-#1.5, #3 |
| GET  | /orders/{orderId} | 200 OK                  | 주문 조회         | REQUIRED-#1.6, #4 |


---

## Required-Level

1. Weighted Load Balancing

2. API Response Header

3. Caching


---

## Challenge-Level

1. Zipkin

2. JWT Authentication failed

3. Caching

4. split configs - dev/prod

5. Layered Architecture

6. OpenFeign Fallback

