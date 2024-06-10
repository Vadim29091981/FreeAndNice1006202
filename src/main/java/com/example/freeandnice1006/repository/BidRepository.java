package com.example.freeandnice1006.repository;


import com.example.freeandnice1006.entities.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepository extends JpaRepository<Bid, Integer> {
    // Дополнительные методы для поиска ставок (при необходимости)
}
