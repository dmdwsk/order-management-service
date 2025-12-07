package com.codedmdwsk.ordermanagementservice.data;

import jakarta.persistence.*;

@Entity
@Table(name = "customers", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="CUSTOMER_NAME", length=50, nullable=false, unique=true)
    private String customerName;
}
