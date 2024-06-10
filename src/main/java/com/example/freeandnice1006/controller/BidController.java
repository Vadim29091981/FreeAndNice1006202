package com.example.freeandnice1006.controller;


import com.example.freeandnice1006.entities.Bid;
import com.example.freeandnice1006.exception.ResourceNotFoundException;
import com.example.freeandnice1006.service.impl.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bids")
public class BidController {

    @Autowired
    private BidService bidService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Bid> getAllBids() {
        return bidService.getAllBids();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Bid> getBidById(@PathVariable int id) {
        Optional<Bid> bid = bidService.getBidById(id);
        if (bid.isPresent()) {
            return ResponseEntity.ok(bid.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Bid> createBid(@RequestBody Bid bid, @RequestParam Long userId,
                                         @RequestParam int auctionId) throws ResourceNotFoundException {
        Bid newBid = bidService.createBid(bid, userId, auctionId);
        return ResponseEntity.ok(newBid);
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Bid> updateBid(@PathVariable int id, @RequestBody Bid bidDetails) {
        Bid updatedBid = bidService.updateBid(id, bidDetails);
        if (updatedBid != null) {
            return ResponseEntity.ok(updatedBid);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteBid(@PathVariable int id) {
        bidService.deleteBid(id);
        return ResponseEntity.noContent().build();
    }
}
