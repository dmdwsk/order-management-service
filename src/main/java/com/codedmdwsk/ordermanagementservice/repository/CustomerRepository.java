package com.codedmdwsk.ordermanagementservice.repository;

import com.codedmdwsk.ordermanagementservice.data.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    boolean existsByCustomerNameIgnoreCase(String customerName);
    boolean existsByCustomerNameIgnoreCaseAndIdNot(String name, Long id);

}
