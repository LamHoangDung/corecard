package com.example.corecard.repository;

import com.example.corecard.entity.CardProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CardProductRepository extends JpaRepository<CardProductEntity, Long> {
    CardProductEntity findByCardProductType(String cardProductType);

}
