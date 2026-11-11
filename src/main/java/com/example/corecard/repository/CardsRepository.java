package com.example.corecard.repository;

import com.example.corecard.entity.CardsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;


@Repository
public interface CardsRepository extends JpaRepository<CardsEntity,Long> {
    CardsEntity findByCardNumberHash(String cardNumber);
    CardsEntity findByCardNumberHashAndCvvHash(String cardNumberHash, String cvvHash);

    @Query(value = "SELECT nextval('card_pan_seq')", nativeQuery = true)
    long nextPanNumber();
}
