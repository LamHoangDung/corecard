package com.example.corecard.repository;

import com.example.corecard.entity.ActivityStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityStatusRepository extends JpaRepository<ActivityStatusEntity,String> {
    ActivityStatusEntity findByCode(String code);
}
