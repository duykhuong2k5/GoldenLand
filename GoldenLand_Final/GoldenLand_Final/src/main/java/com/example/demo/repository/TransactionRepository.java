package com.example.demo.repository;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Transaction;
import com.example.demo.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	List<Transaction> findAllByCodeAndCustomer(String code, Customer customer);
	@Query("""
	        SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t
	        WHERE t.building.id = :buildingId
	          AND t.status = 'SUCCESS'
	    """)
	    Long sumRevenueByBuilding(@Param("buildingId") Long buildingId);
	@Query("""
	        SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t
	        WHERE t.customer.id = :customerId
	          AND t.status = 'SUCCESS'
	    """)
	    Long sumRevenueByCustomer(@Param("customerId") Long customerId);
	Optional<Transaction> findFirstByCodeAndStatusAndNoteContainingOrderByIdDesc(
            String code, String status, String notePart
    );
}
