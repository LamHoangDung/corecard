package com.example.corecard.repository;

import com.example.corecard.entity.CustomersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomersRepository extends JpaRepository<CustomersEntity, Long> {
    CustomersEntity findByCustomerCode(String customerCode);

    @Query(value = "SELECT nextval('credit_customer_seq')", nativeQuery = true)
    long nextCreditCustomerNumber();
}
