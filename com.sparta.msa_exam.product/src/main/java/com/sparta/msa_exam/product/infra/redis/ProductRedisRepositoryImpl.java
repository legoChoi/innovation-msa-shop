package com.sparta.msa_exam.product.infra.redis;

import com.sparta.msa_exam.product.domain.entity.Product;
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
    private final String ALL_KEY = "all";

    @Override
    public List<Product> getProductList() {
        return redisTemplate.opsForList().range(KEY_PREFIX + ALL_KEY, 0, -1);
    }

    @Override
    public void setProductList(List<Product> productList) {
        redisTemplate.opsForList().rightPushAll(KEY_PREFIX + ALL_KEY, productList);
        redisTemplate.expire(KEY_PREFIX + ALL_KEY, Duration.ofMinutes(10)); // TTL
    }

    @Override
    public void addProductList(Product product) {
        redisTemplate.opsForList().rightPush(KEY_PREFIX + ALL_KEY, product);
        redisTemplate.expire(KEY_PREFIX + ALL_KEY, Duration.ofMinutes(10)); // TTL
    }
}
