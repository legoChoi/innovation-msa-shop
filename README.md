## MSA

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
- GET /orders/{orderID}
    ![2-1](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2FcBIzkp%2FbtsPDFsV9fj%2FAAAAAAAAAAAAAAAAAAAAAIkf9txSY_SgE69N_ERmOqhRRhe2uVEVhakTez2uoHAK%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3Df7k%252BVJgGdGoFX1F0uwZF5%252BzD6ik%253D)
    ![2-2](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2FTNI2s%2FbtsPD2ut1a8%2FAAAAAAAAAAAAAAAAAAAAANjOdOSGjiXHo11Y7Ht55Si517pKjV-kxZ0nbnKVG-5y%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3Dh4FgbJe%252FF4SN4mB73vJ2ST3QuzE%253D)

- GET /products
    ![2-3](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2FbnGv6J%2FbtsPFC2BxJj%2FAAAAAAAAAAAAAAAAAAAAAAq7Ogrog5m2jpJj9cGiec8A8HqKKlvEDPHjYmaJLlQr%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3D8KaIuot%252BuRRNTTRs9o4ET9z5tC4%253D)
    ![2-4](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2FbubaFx%2FbtsPGGpVYkf%2FAAAAAAAAAAAAAAAAAAAAAA8pRfVYmVAW_mYC9e8wB5uQnmi8a7rAOJ8cYUpTBE_4%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3D0MhAmFLP1cd6pBIq5Wur9G3fbzU%253D)

3. Caching
    ![3-1](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2FUF4nQ%2FbtsPGowdO1f%2FAAAAAAAAAAAAAAAAAAAAAMRDqi5Z8mlgVFC45-Qy3opyzVh_3NtXQnMHbJZ7zwM2%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3DOXiARU3arTTtZres1oqbSE2D4x4%253D)
    ![3-2](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2FciN1lw%2FbtsPHjamBy4%2FAAAAAAAAAAAAAAAAAAAAALtoLvo-Hihyt8-lHfjjkMFIN20ZLYqWSrl-79A6XThP%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3DRYYLGDy9Q42HZ%252B1q%252FkHZ0pvPU14%253D)
    ![3-3](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2F7vSPy%2FbtsPE6iXZuU%2FAAAAAAAAAAAAAAAAAAAAAHYBBYnsYJPeqt8fkhTfdJkPcTbDcPaXjczAV-JWYB-C%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3D%252F9b1YyIS0d1XWwOeLsqKcDLBkNQ%253D)
    ![3-4](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2FcAcRL1%2FbtsPDWVHqsN%2FAAAAAAAAAAAAAAAAAAAAAJ46To6AbiZyo2fG8vwtNrs2z7druP3ZruDdTdlT318W%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3DK6skxIwxM0IUoK6GFaSsPPRidjY%253D) 

---

## Challenge-Level

1. Zipkin
- POST /orders - success
    ![zipkin-1](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2Fzsoqs%2FbtsPFwaRYZf%2FAAAAAAAAAAAAAAAAAAAAAOCEG6xJgLMiiXD7aKvtzu0DSVARVYicaZDEgfTj9pWW%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3DyhE0cylosodeU%252BUFzn76WDfFVbY%253D)
    ![zipkin-2](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2Fcz6Bqg%2FbtsPHH3Dggh%2FAAAAAAAAAAAAAAAAAAAAAI7KICj_WH32qGDRgp8MMQOVmG-lTL44gmvRhT-sXd9b%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3DH83%252F%252F7kqq1CbCKEJimKAHLnQVCs%253D)

- POST /orders - fail
    ![zipkin-3](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2Fbk8xFy%2FbtsPFhZsgs2%2FAAAAAAAAAAAAAAAAAAAAANefhSf2IxG0Q97a3GeSYUcvnFgNN_jtGuWBl-gH5NJs%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3D%252BMr5%252FDEJWGTIfBNquFp2u%252FbVGJA%253D)
    ![zipkin-4](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2FcWXpaH%2FbtsPGmr27c5%2FAAAAAAAAAAAAAAAAAAAAAEYauQnux9Rk6aZvg6LNrEVQ_8iPwXg5ze0tPSGcnJQ-%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3DLJpArVL9wGVUQ0qZi3swBIG82YM%253D)
    

2. JWT Authentication failed

3. Caching

4. split configs - dev/prod

5. Layered Architecture

6. OpenFeign Fallback

