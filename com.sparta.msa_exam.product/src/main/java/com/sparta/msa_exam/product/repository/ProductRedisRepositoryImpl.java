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

    private final RedisTemplate<String, Product> redisTemplate;

    private final String KEY_PREFIX = "products::";

    @Override
    public List<Product> getProductList() {
        return redisTemplate.opsForList().range(KEY_PREFIX, 0, -1);
    }

    @Override
    public void setProductList(List<Product> productList) {
        redisTemplate.opsForList().rightPushAll(KEY_PREFIX, productList);
        redisTemplate.expire(KEY_PREFIX, Duration.ofMinutes(10));
    }

    @Override
    public void addProductList(Product product) {
        redisTemplate.opsForList().rightPush(KEY_PREFIX, product);
        redisTemplate.expire(KEY_PREFIX, Duration.ofMinutes(10));
    }
}
