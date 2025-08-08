# MSA

> https://itak.tistory.com/24

## Services

| Service             | Description             | Skills                                                             | Port         |
|---------------------|-------------------------|--------------------------------------------------------------------|--------------|
| **Eureka Server**   | 서비스 등록 및 헬스 체크          | Spring Cloud (Netflix Eureka Server, Actuator)                     | 19090        |
| **API Gateway**     | 외부 요청 라우팅, JWT 인증/인가 처리 | Spring Cloud (Gateway, Actuator), JWT                              | 19091        |
| **Order Service**   | 주문  관리                  | Spring Cloud (Client, Actuator), OpenFeign, JPA, MySQL, Redis      | 19092        |
| **Product Service** | 상품 관리                   | Spring Cloud (Client, Actuator), JPA, MySQL, Redis                 | 19093, 19094 |
| **Auth Service**    | 회원 가입, 로그인              | Spring Cloud (Client, Actuator), OpenFeign, JPA, MySQL, Redis, JWT | 19095        |
| **User Service**    | 유저 관리                   | Spring Cloud (Client, Actuator), JPA, MySQL                        | 19096        |

---

## API Specification

- **Products**

| HTTP | Path       | Response Status | Description |               |
|------|------------|-----------------|-------------|---------------|
| POST | /products  | 201 CREATED     | 상품 추가       | REQUIRED-#1.1 |
| GET  | /products  | 200 OK          | 상품 목록 조회    | REQUIRED-#1.2 |

- **Products(Feign)**

| HTTP | Path                            | Description               |
|------|---------------------------------|---------------------------|
| POST | /feign/products/validation?fail | 상품 정합성 검증, 서비스 에러(실패 케이스) |

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

- **Users(Feign)**

| HTTP | Path                         | Description |
|------|------------------------------|-------------|
| POST | /feign/users                 | 유저 생성       |
| GET  | /feign/users?userId&username | 유저 조회       |


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

![authentication-fail](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2Fb4j6Gg%2FbtsPE8H6T8p%2FAAAAAAAAAAAAAAAAAAAAAG-xLWY77GS-ayoVB1Uyk1KHE_49fEo9WaxBRTds-X1b%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3DUPhJFXxPDeYgcvCTobuHoLciHdI%253D)

3. Caching
- 사용자가 상품 목록 조회 API 요청 시 상품 목록이 캐싱되어 해당 API를 재요청 할 경우 빠른 응답을 반환한다.
- 사용자가 상품 생성 API 요청 시 캐싱되어 있는 상품 목록에 추가된 상품이 추가로 캐싱.

```
@Transactional
public ProductCreateResponse createProduct(ProductCreateRequest request) {
    Product product = new Product(request.name(), request.price());
    productJpaRepository.save(product);

    // 캐싱된 상품 목록 확인
    List<Product> productList = productRedisRepository.getProductList();

    // 캐싱된 상품 목록이 존재하면 상품 목록에 추가된 상품 추가 캐싱
    if (!productList.isEmpty()) {
        productRedisRepository.addProductList(product);
    }

    return new ProductCreateResponse(product.getId(), product.getName(), product.getSupplyPrice());
}


// ProductRedisRepositoryImpl

@Override
public void addProductList(Product product) {
    redisTemplate.opsForList().rightPush(KEY_PREFIX + ALL_KEY, product);
    redisTemplate.expire(KEY_PREFIX + ALL_KEY, Duration.ofMinutes(10));
}
```
- 아무것도 캐싱되어 있지 않았을 때 상품 생성 API 요청시 다음과 같이 캐싱되지 않음.

![caching-3.1](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2FdjzVWU%2FbtsPN4cWzD1%2FAAAAAAAAAAAAAAAAAAAAAD4RIaxhZK0_c8pz-GLUkVxUpruDGeZpcccdBOejiglP%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3DG7Cr13mFhtoP0m02kaCmeQu6i9w%253D)
![caching-3.2](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2FbceWOT%2FbtsPNco0QPT%2FAAAAAAAAAAAAAAAAAAAAADWsCvCBX_espod2hVHn_gRgiBqyauJ1r46uof3jIMuh%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3DQAdHZpXDbrBzQwE6IAKULzArXDg%253D)

- 하지만 상품 목록 API 요청을 통해 상품 목록을 캐싱 후 상품 생성 API를 요청 할 경우
- 다음과 같이 다음과 같이 추가된 상품이 캐싱된 상품 목록에 추가된다.

![caching-3.3](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2FCZFVT%2FbtsPObwmDpn%2FAAAAAAAAAAAAAAAAAAAAAAbaNr35bFAehuuKmMSedf-fyzUBehmbs63n9_mNHEOS%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3Dmn8JtdTDKchoMdKgqqQxNHR95ec%253D)
![caching-3.4](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2FowHLy%2FbtsPN4jIjm6%2FAAAAAAAAAAAAAAAAAAAAAExS5JZtaEh8xumCKplOypuE2n5GNMN6jvmm7ZZa6Q9d%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3DLGx9UWqq127fVRGa65mqk%252B4lBsg%253D)
![caching-3.5](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2F2Tp6j%2FbtsPKRfCrCx%2FAAAAAAAAAAAAAAAAAAAAAIQZ15ufcIS2zNQ2hIDv5K_fh3fwJobS0DeILiyYz7cQ%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3DYdWxGrJa3RfUjyTwbx3v9GzwjO0%253D)
![caching-3.6](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2FbzqHA5%2FbtsPLX0mJeA%2FAAAAAAAAAAAAAAAAAAAAAACPo7hxrBJAL5X1tQwavyscp04iOVHi9vBhsc1NuJP3%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3D72xr2iyiDCK9kLQ3Agft2xqODdA%253D)



4. split configs - dev/prod

![order-application.yml](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2Fbl8CMT%2FbtsPGlNsUid%2FAAAAAAAAAAAAAAAAAAAAAKMLZ2jvbVjx_0nhi5vdTMSqDp2dmJ6_QzaVJ_SxxhZv%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3DeivcKLAnnQeGDu0d2zOuNeirihg%253D)
![order-application-dev.yml](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2Fn2LdP%2FbtsPHgrB5IE%2FAAAAAAAAAAAAAAAAAAAAAFtuqnT75krWYU60yKq4qxyWOMPyxj8PWgH1BiLopaPI%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3DSW2QnWXGyo%252BAHqVBWbK9Bbox6Tc%253D)
![order-application-prod.yml](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2FeGTCSS%2FbtsPIe7LPz6%2FAAAAAAAAAAAAAAAAAAAAAJk17bb3GXLraiebY_EeNC1lXFo0fg4V0b7bFV6foPTE%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3DQRy9P1xi43RXIadZWf%252Fx0DAcFAU%253D)

5. Layered Architecture

6. OpenFeign Fallback
- Order-Service의 상품 생성시 Feign을 통한 통신시 Product-Service의 응답 코드 별 예외 처리  
- #1 Product Not Found

![fallback-6.1](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2FeB9kil%2FbtsPOpOXAsN%2FAAAAAAAAAAAAAAAAAAAAAHjMzOAdF5uDyzr3smtu5HVgVI6hh47npWSQTawaAj4V%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3D9oeL0BgC2Lz88tuwEOhMKs3TLLc%253D)
![fallback-6.2](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2FcprF5Z%2FbtsPMRrZlel%2FAAAAAAAAAAAAAAAAAAAAADRMVEDjgwMJmbccIZghClCLEwyN_YUu25Uk_Tnl9JAn%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3DhT5Vuo5MPSjaa%252F2jrr7CamK0op0%253D)
![fallback-6.3](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2Fb29V4U%2FbtsPMVumbDx%2FAAAAAAAAAAAAAAAAAAAAAG-siIQp3I-aNog2WVOBoQqDkmOYxqI-9LDEuNO368xf%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3Dn14%252BE0cW04OSRA%252B7HQL%252BBAD%252BvRU%253D)

- #2 Product Service Unavailable

![fallback-6.4](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2FwbaFK%2FbtsPNl7ncRx%2FAAAAAAAAAAAAAAAAAAAAAEWqU2UBDCHkPRX7IOda4d7aUH8kYrzZAWZWHis6wLuF%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3D2UrLtZUIq9Kez9%252BF%252BaSgOu%252Fli7k%253D)
![fallback-6.5](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2F9Q7f7%2FbtsPM1nNsTC%2FAAAAAAAAAAAAAAAAAAAAAIY2UMvMfUYKrlFTqEPARBzisHtWyI44bm9Tf-iUs6U8%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3Dx2aurxvyZxupP7oWa%252FJAu%252BL465s%253D)
![fallback-6.6](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdna%2FssbhC%2FbtsPNPAnfXS%2FAAAAAAAAAAAAAAAAAAAAAOwKdexqquDPk5K0V9AxLN2r2fV-JkVsWfvHgMlW_NE7%2Fimg.png%3Fcredential%3DyqXZFxpELC7KVnFOS48ylbz2pIh7yKj8%26expires%3D1756652399%26allow_ip%3D%26allow_referer%3D%26signature%3D84KStm7cbJ%252BB1%252FIV5rdlNJYMyWE%253D)
