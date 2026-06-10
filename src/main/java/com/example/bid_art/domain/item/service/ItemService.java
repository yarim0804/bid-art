package com.example.bid_art.domain.item.service;


import com.example.bid_art.domain.item.repository.Item;
import com.example.bid_art.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용으로 설정 (성능 최적화)
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * 전체 물품 조회
     */
    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }

    /**
     * 물품 등록
     */
    @Transactional // 쓰기 작업이므로 별도의 설정 추가
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    /**
     * 단건 조회
     */
    public Item findItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 물품이 존재하지 않습니다. id=" + id));
    }
}