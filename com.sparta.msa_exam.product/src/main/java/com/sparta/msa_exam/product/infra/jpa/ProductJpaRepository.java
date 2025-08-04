package com.sparta.msa_exam.product.infra.jpa;

import com.sparta.msa_exam.product.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {
}
