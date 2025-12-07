package com.codedmdwsk.ordermanagementservice.data;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "orders")
public class OrderData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private Long totalPrice;
    private String products;
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

}

