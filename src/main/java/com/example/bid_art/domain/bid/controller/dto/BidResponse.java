package com.example.bid_art.domain.bid.controller.dto;

import com.example.bid_art.domain.bid.repository.Bid;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BidResponse {
    private Long id;
    private Long itemId;
    private String bidderName;
    private int amount;
    private LocalDateTime createdAt;

    public static BidResponse from(Bid bid) {
        return new BidResponse(
                bid.getId(),
                bid.getItem().getId(),
                bid.getBidderName(),
                bid.getAmount(),
                bid.getCreatedAt()
        );
    }
}
