package com.codedmdwsk.ordermanagementservice.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customers", uniqueConstraints = {
        @UniqueConstraint(columnNames = "CUSTOMER_NAME")
})
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="CUSTOMER_NAME", length=50, nullable=false, unique=true)
    private String customerName;
}
