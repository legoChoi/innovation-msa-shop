package com.sparta.msa_exam.product.repository;

import com.sparta.msa_exam.product.entity.Product;

import java.util.List;

public interface ProductRedisRepository {

    List<Product> getProductList();

    void setProductList(List<Product> productList);

    void addProductList(Product product);
}
