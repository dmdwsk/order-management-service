package com.codedmdwsk.ordermanagementservice.repository;

import com.codedmdwsk.ordermanagementservice.data.OrderData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderData,Long> {
}
