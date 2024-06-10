package com.example.freeandnice1006.service.impl;


import com.example.freeandnice1006.entities.Auction;
import com.example.freeandnice1006.entities.Item;
import com.example.freeandnice1006.exception.ResourceNotFoundException;
import com.example.freeandnice1006.repository.AuctionRepository;
import com.example.freeandnice1006.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private ItemRepository itemRepository;

    public List<Auction> getAllAuctions() {
        return auctionRepository.findAll();
    }

    public Optional<Auction> getAuctionById(int id) {
        return auctionRepository.findById(id);
    }

    public Auction createAuction(Auction auction, int itemId) throws ResourceNotFoundException {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found for this id :: " + itemId));
        auction.setItem(item);
        return auctionRepository.save(auction);
    }

    public Auction updateAuction(int id, Auction auctionDetails) {
        Optional<Auction> auctionOptional = auctionRepository.findById(id);
        if (auctionOptional.isPresent()) {
            Auction auction = auctionOptional.get();
            auction.setItem(auctionDetails.getItem());
            auction.setBids(auctionDetails.getBids());
            // Сохраняем обновленный аукцион
            return auctionRepository.save(auction);
        } else {
            // Обработка случая, когда аукцион не найден
            return null;
        }
    }

    public void deleteAuction(int id) {
        auctionRepository.deleteById(id);
    }
}
