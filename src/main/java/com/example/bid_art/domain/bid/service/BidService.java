package com.example.bid_art.domain.bid.service;

import com.example.bid_art.domain.bid.controller.dto.BidRequest;
import com.example.bid_art.domain.bid.controller.dto.BidResponse;
import com.example.bid_art.domain.bid.repository.Bid;
import com.example.bid_art.domain.bid.repository.BidRepository;
import com.example.bid_art.domain.item.repository.Item;
import com.example.bid_art.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BidService {

    private final BidRepository bidRepository;
    private final ItemRepository itemRepository;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 입찰 처리.
     * 1) 물품을 비관적 락으로 조회해 동시 입찰 경합을 막는다.
     * 2) 현재가보다 높은지 검증하며 현재가를 갱신한다.
     * 3) 입찰 이력을 저장하고, 구독 중인 모든 클라이언트에게 실시간 전파한다.
     */
    @Transactional
    public BidResponse placeBid(Long itemId, BidRequest request) {
        Item item = itemRepository.findByIdForUpdate(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 물품이 존재하지 않습니다. id=" + itemId));

        item.placeBid(request.getAmount()); // 현재가 검증 + 갱신

        Bid bid = bidRepository.save(new Bid(item, request.getBidderName(), request.getAmount()));
        BidResponse response = BidResponse.from(bid);

        // 해당 물품을 구독 중인 모든 사용자에게 실시간 브로드캐스트
        messagingTemplate.convertAndSend("/topic/items/" + itemId, response);

        return response;
    }

    /**
     * 특정 물품의 입찰 이력 조회 (최신순)
     */
    public List<BidResponse> getBids(Long itemId) {
        return bidRepository.findByItemIdOrderByCreatedAtDesc(itemId).stream()
                .map(BidResponse::from)
                .toList();
    }
}
