// src/main/java/com/example/demo/repository/CustomerRepositoryCustom.java
package com.example.demo.repository.custom.impl;

import com.example.demo.entity.Customer;
import java.util.List;
import java.util.Map;

public interface CustomerRepositoryCustom {
    List<Customer> findAll(Map<String, Object> conditions);
    // nếu bạn muốn bản phân trang:
    // Page<Customer> findAll(Map<String,Object> conditions, Pageable pageable);
}
