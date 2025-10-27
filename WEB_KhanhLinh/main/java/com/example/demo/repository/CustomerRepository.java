package com.example.demo.repository;

import com.example.demo.entity.Customer;
import com.example.demo.repository.custom.CustomerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryCustom {
	boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Customer findByUsername(String username);
}
