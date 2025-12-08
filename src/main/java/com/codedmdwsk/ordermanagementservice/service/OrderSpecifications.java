package com.codedmdwsk.ordermanagementservice.service;

import com.codedmdwsk.ordermanagementservice.data.OrderData;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecifications {
    public static Specification<OrderData> byCustomerId(Long customerId){
        if (customerId == null) {
            return null;
        }
        return (root, query, cb) ->
                cb.equal(root.get("customer").get("id"), customerId);
    }
    public static Specification<OrderData> byProduct(String products) {
        if (products == null || products.isBlank()) return null;
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("products")), "%" + products.toLowerCase() + "%");
    }
}
