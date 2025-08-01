package com.sparta.msa_exam.product.repository;

import com.sparta.msa_exam.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRedisRepositoryImpl implements ProductRedisRepository {

    private final RedisTemplate<String, Product> productListRedisTemplate;

    private final String KEY_PREFIX = "products::";

    @Override
    public List<Product> getProductAll() {
        return productListRedisTemplate.opsForList().range(KEY_PREFIX, 0, -1);
    }

    @Override
    public void setProductAll(List<Product> productList) {
        productListRedisTemplate.opsForList().leftPushAll(KEY_PREFIX, productList);
        productListRedisTemplate.expire(KEY_PREFIX, Duration.ofMinutes(10));
    }

    @Override
    public void addProduct(Product product) {
        productListRedisTemplate.opsForList().rightPush(KEY_PREFIX, product);
        productListRedisTemplate.expire(KEY_PREFIX, Duration.ofMinutes(10));
    }
}
