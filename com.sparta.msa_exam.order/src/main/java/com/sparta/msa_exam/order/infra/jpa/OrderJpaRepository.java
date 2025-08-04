package com.sparta.msa_exam.order.infra.jpa;

import com.sparta.msa_exam.order.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
