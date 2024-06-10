package com.example.freeandnice1006.repository;


import com.example.freeandnice1006.entities.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Integer> {
    // Дополнительные методы для поиска аукционов (при необходимости)
}
