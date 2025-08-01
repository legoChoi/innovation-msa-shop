package com.sparta.msa_exam.product.repository;

import com.sparta.msa_exam.product.entity.Product;

import java.util.List;

public interface ProductRedisRepository {

    List<Product> getProductAll();

    void setProductAll(List<Product> productList);

    void addProduct(Product product);
}
