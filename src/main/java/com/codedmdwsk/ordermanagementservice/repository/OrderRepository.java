package com.codedmdwsk.ordermanagementservice.repository;

import com.codedmdwsk.ordermanagementservice.data.OrderData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderRepository
        extends JpaRepository<OrderData, Long>, JpaSpecificationExecutor<OrderData> {
}
