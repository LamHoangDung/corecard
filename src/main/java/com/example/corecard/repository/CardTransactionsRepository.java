package com.example.corecard.repository;

import com.example.corecard.entity.CardTransactionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardTransactionsRepository extends JpaRepository<CardTransactionsEntity, Long> {

}
