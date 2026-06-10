package com.example.bid_art.domain.bid.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {
    // 특정 물품의 입찰 이력을 최신순으로 조회
    List<Bid> findByItemIdOrderByCreatedAtDesc(Long itemId);
}
