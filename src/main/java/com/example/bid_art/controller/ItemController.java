package com.example.bid_art.controller;

import com.example.bid_art.domain.item.Item;
import com.example.bid_art.domain.item.ItemRepository;
import com.example.bid_art.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {
    private final ItemRepository itemRepository;

    @GetMapping("/api/items")
    public ApiResponse<List<Item>> getItems() {
        return ApiResponse.success(itemRepository.findAll());
    }

    @PostMapping("/api/items")
    public ApiResponse<Item> saveItem(@RequestBody Item item) {
        // 실제로는 Service 계층을 둬야 하지만, 우선 바로 저장해봅니다.
        Item savedItem = itemRepository.save(item);
        return ApiResponse.success(savedItem);
    }
}