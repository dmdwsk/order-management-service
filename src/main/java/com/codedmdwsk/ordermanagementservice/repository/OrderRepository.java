package com.codedmdwsk.ordermanagementservice.repository;

import com.codedmdwsk.ordermanagementservice.data.OrderData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrderRepository
        extends JpaRepository<OrderData, Long>, JpaSpecificationExecutor<OrderData> {
    @Override
    @EntityGraph(attributePaths = "customer")
    Page<OrderData> findAll(Specification<OrderData> spec, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "customer")
    List<OrderData> findAll(Specification<OrderData> spec);
}
